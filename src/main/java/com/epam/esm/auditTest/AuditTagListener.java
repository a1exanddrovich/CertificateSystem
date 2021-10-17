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
public class AuditTagListener {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String PERSIST = "Persist";
    private static final String UPDATE = "Update";

    private EntityManager manager;

    @Autowired
    public AuditTagListener(EntityManager entityManager) {
        this.manager = entityManager;
    }

    public AuditTagListener() { }

    @PreUpdate
    public void preUpdate(Object tag) {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableTagTest auditableTagTest = (AuditableTagTest) tag;
        auditableTagTest.setOperationType(UPDATE);
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));;
        auditableTagTest.setOperationTimeStamp(timestamp);
        auditableTagTest.setEntityId(((AuditableTagTest) tag).getId());
        manager.merge(auditableTagTest);
    }

    @PostPersist
    public void postPersist(Object tag) {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableTagTest auditableTagTest = (AuditableTagTest) tag;
        auditableTagTest.setOperationType(PERSIST);
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));;
        auditableTagTest.setOperationTimeStamp(timestamp);
        auditableTagTest.setEntityId(((AuditableTagTest) tag).getId());
        manager.persist(auditableTagTest);
    }

}
