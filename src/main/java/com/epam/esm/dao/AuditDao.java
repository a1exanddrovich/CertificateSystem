package com.epam.esm.dao;

import com.epam.esm.audit.AuditableEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AuditDao {

    @PersistenceContext
    private EntityManager manager;

    public void saveEntity(AuditableEntity entity) {
        manager.persist(entity);
    }

    public void updateEntity(AuditableEntity entity) {
        manager.merge(entity);
    }

}
