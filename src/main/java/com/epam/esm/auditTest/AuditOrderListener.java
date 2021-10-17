package com.epam.esm.auditTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AuditOrderListener {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String PERSIST = "Persist";
    private static final String UPDATE = "Update";

    private EntityManager manager;

    @Autowired
    public AuditOrderListener(EntityManager entityManager) {
        this.manager = entityManager;
    }

    public AuditOrderListener() { }

    @PreUpdate
    public void preUpdate(Object user) {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableOrderTest auditableOrderTest = (AuditableOrderTest) user;
        auditableOrderTest.setOperationType(UPDATE);
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));;
        auditableOrderTest.setOperationTimeStamp(timestamp);
        auditableOrderTest.setEntityId(((AuditableOrderTest) user).getId());
        manager.merge(auditableOrderTest);
    }

    @PostPersist
    public void postPersist(Object user) {
        //SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableOrderTest auditableOrderTest = (AuditableOrderTest) user;
        auditableOrderTest.setOperationType(PERSIST);
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));;
        auditableOrderTest.setOperationTimeStamp(timestamp);
        auditableOrderTest.setEntityId(((AuditableOrderTest) user).getId());
        manager.persist(auditableOrderTest);
    }

}
