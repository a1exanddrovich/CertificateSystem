package com.epam.esm.auditTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AuditGiftCertificateListener {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String PERSIST = "Persist";
    private static final String UPDATE = "Update";

    private EntityManager manager;

    @Autowired
    public AuditGiftCertificateListener(EntityManager entityManager) {
        this.manager = entityManager;
    }

    public AuditGiftCertificateListener() { }

    @PreUpdate
    public void preUpdate(Object giftCertificate) {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableGiftCertificateTest auditableGiftCertificateTest = (AuditableGiftCertificateTest) giftCertificate;
        auditableGiftCertificateTest.setOperationType(UPDATE);
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));;
        auditableGiftCertificateTest.setOperationTimeStamp(timestamp);
        auditableGiftCertificateTest.setEntityId(((AuditableGiftCertificateTest) giftCertificate).getId());
        manager.merge(auditableGiftCertificateTest);
    }

    @PostPersist
    public void postPersist(Object giftCertificate) {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableGiftCertificateTest auditableGiftCertificateTest = (AuditableGiftCertificateTest) giftCertificate;
        auditableGiftCertificateTest.setOperationType(PERSIST);
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));;
        auditableGiftCertificateTest.setOperationTimeStamp(timestamp);
        auditableGiftCertificateTest.setEntityId(((AuditableGiftCertificateTest) giftCertificate).getId());
        manager.persist(auditableGiftCertificateTest);
    }

}
