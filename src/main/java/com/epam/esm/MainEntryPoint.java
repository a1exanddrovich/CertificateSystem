package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
public class MainEntryPoint {
    private static final String ENCODING = "UTF-8";
    private static final String SET_LOCALE = "classpath:locale/messages";

    public static void main(String[] args) {
        SpringApplication.run(MainEntryPoint.class, args);
    }

    @Bean
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasename(SET_LOCALE);
        reloadableResourceBundleMessageSource.setDefaultEncoding(ENCODING);

        return reloadableResourceBundleMessageSource;
    }
}
