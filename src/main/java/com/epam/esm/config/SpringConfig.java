package com.epam.esm.config;

import com.epam.esm.audit.AuditableEntity;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.Properties;

@SpringBootConfiguration
public class SpringConfig {

    private static final String ENCODING = "UTF-8";
    private static final String SET_LOCALE = "classpath:locale/messages";

    @Bean
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasename(SET_LOCALE);
        reloadableResourceBundleMessageSource.setDefaultEncoding(ENCODING);

        return reloadableResourceBundleMessageSource;
    }

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
        mutablePersistenceUnitInfo.addManagedClassName(AuditableEntity.class.getName());
        PersistenceUnitDescriptor persistenceUnitDescriptor = new PersistenceUnitInfoDescriptor(
                mutablePersistenceUnitInfo);
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = new EntityManagerFactoryBuilderImpl(
                persistenceUnitDescriptor, Collections.emptyMap());
        return entityManagerFactoryBuilder.build();
    }


}
