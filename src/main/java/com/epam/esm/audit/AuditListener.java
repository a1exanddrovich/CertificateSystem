package com.epam.esm.audit;

import com.epam.esm.entity.*;
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
public class AuditListener {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String PERSIST = "Persist";
    private static final String UPDATE = "Update";
    private static final String NOT_AUDITABLE_ENTITY = "Not auditable entity passed";

    private static EntityManager manager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        manager = entityManager;
    }

    public AuditListener() { }

    @PreUpdate
    private void preUpdating(Identifiable entity) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableEntity<?> auditable = createCertainAuditableEntity(entity);
        setTimeStamp(auditable);
        auditable.setOperationType(UPDATE);

        manager.merge(auditable);
    }

    @PostPersist
    private void postPersisting(Identifiable entity) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableEntity<?> auditable = createCertainAuditableEntity(entity);
        setTimeStamp(auditable);
        auditable.setOperationType(PERSIST);

        manager.persist(auditable);
    }

    private void setTimeStamp(AuditableEntity<?> entity) {
        ZonedDateTime timestamp = ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        entity.setOperationTimeStamp(timestamp);
    }

    private AuditableEntity<?> createCertainAuditableEntity(Identifiable entity) {
        if (entity.getClass() == GiftCertificate.class) {
            return new AuditableGiftCertificate((GiftCertificate) entity);
        }

        if (entity.getClass() == User.class) {
            return new AuditableUser((User) entity);
        }

        if (entity.getClass() == Order.class) {
            return new AuditableOrder((Order) entity);
        }

        if (entity.getClass() == Tag.class) {
            return new AuditableTag((Tag) entity);
        }

        throw new IllegalArgumentException(NOT_AUDITABLE_ENTITY);
    }

}
