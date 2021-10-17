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
public class AuditUserListener {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String PERSIST = "Persist";
    private static final String UPDATE = "Update";

    private EntityManager manager;

    @Autowired
    public AuditUserListener(EntityManager entityManager) {
        this.manager = entityManager;
    }

    public AuditUserListener() { }

    @PreUpdate
    public void preUpdate(Object user) {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableUserTest auditableUserTest = (AuditableUserTest) user;
        auditableUserTest.setOperationType(UPDATE);
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));;
        auditableUserTest.setOperationTimeStamp(timestamp);
        auditableUserTest.setEntityId(((AuditableUserTest) user).getId());
        manager.merge(auditableUserTest);
    }

    @PostPersist
    public void postPersist(Object user) {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableUserTest auditableUserTest = (AuditableUserTest) user;
        auditableUserTest.setOperationType(PERSIST);
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));;
        auditableUserTest.setOperationTimeStamp(timestamp);
        auditableUserTest.setEntityId(((AuditableUserTest) user).getId());
        manager.persist(auditableUserTest);
    }

}
