package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@PropertySource("classpath:database.properties")
@PropertySource("classpath:timezone.properties")
@EnableTransactionManagement
public class SpringConfig implements WebMvcConfigurer {

    private static final String DATABASE_PROD_DRIVER = "database.prod.driver";
    private static final String DATABASE_PROD_URL = "database.prod.url";
    private static final String DATABASE_PROD_USER = "database.prod.user";
    private static final String DATABASE_PROD_PASSWORD = "database.prod.password";
    private static final String ENCODING = "UTF-8";

    private final ApplicationContext applicationContext;
    private final Environment environment;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
    }

    @Bean
    @Profile("dev")
    public PlatformTransactionManager devTransactionManager() {
        return new DataSourceTransactionManager(devDataSource());
    }

    @Bean
    @Profile("prod")
    public PlatformTransactionManager prodTransactionManager() {
        return new DataSourceTransactionManager(prodDataSource());
    }

    @Bean
    @Profile("dev")
    public JdbcTemplate devJdbcTemplate() {
        return new JdbcTemplate(devDataSource());
    }

    @Bean
    @Profile("prod")
    public JdbcTemplate prodJdbcTemplate() {
        return new JdbcTemplate(prodDataSource());
    }

    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return devDataSourceConfig().setUp();
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return prodDataSourceConfig().setUp();
    }

    @Bean
    @Profile("dev")
    public DataSourceConfig devDataSourceConfig() {
        return new DevDataSourceConfig();
    }

    @Bean
    @Profile("prod")
    public DataSourceConfig prodDataSourceConfig() {
        return new ProdDataSourceConfig(environment);
    }

    @Bean
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasename("classpath:locale/messages");
        reloadableResourceBundleMessageSource.setCacheSeconds(-1);
        reloadableResourceBundleMessageSource.setUseCodeAsDefaultMessage(false);
        reloadableResourceBundleMessageSource.setDefaultEncoding(ENCODING);

        return reloadableResourceBundleMessageSource;
    }

}
