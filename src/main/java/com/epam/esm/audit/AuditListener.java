package com.epam.esm.audit;

import com.epam.esm.entity.Identifiable;
import com.epam.esm.utils.Constants;
import com.epam.esm.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;

@Component
public class AuditListener {

    private static final String PERSIST = "Persist";
    private static final String UPDATE = "Update";

    private static EntityManager manager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        manager = entityManager;
    }

    @PreUpdate
    private void preUpdating(Identifiable entity) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableEntity auditable = new AuditableEntity(entity.getId(), entity.getType(), UPDATE);
        setTimeStamp(auditable);

        manager.merge(auditable);
    }

    @PostPersist
    private void postPersisting(Identifiable entity) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableEntity auditable = new AuditableEntity(entity.getId(), entity.getType(), PERSIST);
        setTimeStamp(auditable);

        manager.persist(auditable);
    }

    private void setTimeStamp(AuditableEntity entity) {
        entity.setOperationTimeStamp(DateTimeUtils.now(Constants.DATE_FORMAT));
    }

}
