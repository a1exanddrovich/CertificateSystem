package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@EnableTransactionManagement
public class SpringConfig implements WebMvcConfigurer {

    private static final String ENCODING = "UTF-8";
    private static final String SET_LOCALE = "classpath:locale/messages";
    private static final String ACCEPTED_LANGUAGE = "Accept-language";

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasename(SET_LOCALE);
        reloadableResourceBundleMessageSource.setCacheSeconds(-1);
        reloadableResourceBundleMessageSource.setUseCodeAsDefaultMessage(false);
        reloadableResourceBundleMessageSource.setDefaultEncoding(ENCODING);

        return reloadableResourceBundleMessageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String acceptedLocale = request.getHeader(ACCEPTED_LANGUAGE);

                return (acceptedLocale == null || acceptedLocale.trim().isEmpty()) ? super.determineDefaultLocale(request)
                                                                                   : request.getLocale();

            }
        };
    }

}
