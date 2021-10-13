package com.epam.esm.config;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;

@SpringBootConfiguration
//@EnableWebMvc
//@EnableTransactionManagement
//@EnableAutoConfiguration
//@PropertySource("classpath:application.properties")
public class SpringConfig implements WebMvcConfigurer {

    private static final String ENCODING = "UTF-8";
    private static final String SET_LOCALE = "classpath:locale/messages";
    private static final String ACCEPTED_LANGUAGE = "Accept-language";
    private static final String PERSISTENCE_UNIT_NAME = "app";

//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
    @Bean
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasename(SET_LOCALE);
        reloadableResourceBundleMessageSource.setCacheSeconds(-1);
        reloadableResourceBundleMessageSource.setUseCodeAsDefaultMessage(false);
        reloadableResourceBundleMessageSource.setDefaultEncoding(ENCODING);

        return reloadableResourceBundleMessageSource;
    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        return new CookieLocaleResolver() {
//            @Override
//            public Locale resolveLocale(HttpServletRequest request) {
//                String acceptedLocale = request.getHeader(ACCEPTED_LANGUAGE);
//
//                return (acceptedLocale == null || acceptedLocale.trim().isEmpty()) ? super.determineDefaultLocale(request)
//                                                                                   : request.getLocale();
//
//            }
//        };
//    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        MutablePersistenceUnitInfo mutablePersistenceUnitInfo = new MutablePersistenceUnitInfo() {
            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
        mutablePersistenceUnitInfo.setPersistenceUnitName("App");
        mutablePersistenceUnitInfo.setPersistenceProviderClassName(HibernatePersistenceProvider.class.getName());
        Properties props = new Properties();
        props.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        props.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/giftcertificatesdatabase?serverTimezone=UTC");
        props.put("javax.persistence.jdbc.user", "root");
        props.put("javax.persistence.jdbc.password", "");
        mutablePersistenceUnitInfo.setProperties(props);
        mutablePersistenceUnitInfo.addManagedClassName(Tag.class.getName());
        mutablePersistenceUnitInfo.addManagedClassName(GiftCertificate.class.getName());
        mutablePersistenceUnitInfo.addManagedClassName(User.class.getName());
        mutablePersistenceUnitInfo.addManagedClassName(Order.class.getName());
        PersistenceUnitDescriptor persistenceUnitDescriptor = new PersistenceUnitInfoDescriptor(
                mutablePersistenceUnitInfo);
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = new EntityManagerFactoryBuilderImpl(
                persistenceUnitDescriptor, Collections.EMPTY_MAP);
        EntityManagerFactory entityManagerFactory = entityManagerFactoryBuilder.build();
        return entityManagerFactory;
//        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//        Persistence.createEntityManagerFactory();
    }
//
//    @Bean
//    @Primary
//    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
//        return entityManagerFactory.createEntityManager();
//    }

}
