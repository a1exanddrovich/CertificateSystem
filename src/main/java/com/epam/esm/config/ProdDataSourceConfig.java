package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Profile("prod")
@Configuration
@PropertySource("classpath:database-prod.properties")
public class ProdDataSourceConfig {

    private static final String DATABASE_PROD_DRIVER = "database.prod.driver";
    private static final String DATABASE_PROD_URL = "database.prod.url";
    private static final String DATABASE_PROD_USER = "database.prod.user";
    private static final String DATABASE_PROD_PASSWORD = "database.prod.password";

    private final Environment environment;

    @Autowired
    public ProdDataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getProperty(DATABASE_PROD_DRIVER));
        dataSource.setUrl(environment.getProperty(DATABASE_PROD_URL));
        dataSource.setUsername(environment.getProperty(DATABASE_PROD_USER));
        dataSource.setPassword(environment.getProperty(DATABASE_PROD_PASSWORD));

        return dataSource;
    }

}
