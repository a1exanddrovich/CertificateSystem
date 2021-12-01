package com.epam.esm.audit;

import com.epam.esm.dao.AuditDao;
import com.epam.esm.entity.Identifiable;
import com.epam.esm.utils.Constants;
import com.epam.esm.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;

@Component
public class AuditListener {

    private static final String PERSIST = "Persist";
    private static final String UPDATE = "Update";

    @Autowired
    private AuditDao dao;

    public AuditListener() { }

    @PreUpdate
    private void preUpdating(Identifiable entity) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        AuditableEntity auditableEntity = new AuditableEntity(entity.getId(), entity.getType(), UPDATE);
        setTimeStamp(auditableEntity);

        dao.updateEntity(auditableEntity);
    }

    @PostPersist
    private void postPersisting(Identifiable entity) {
        AuditableEntity auditableEntity = new AuditableEntity(entity.getId(), entity.getType(), PERSIST);
        setTimeStamp(auditableEntity);

        dao.saveEntity(auditableEntity);
    }

    private void setTimeStamp(AuditableEntity entity) {
        entity.setOperationTimeStamp(DateTimeUtils.now(Constants.DATE_FORMAT));
    }

}
