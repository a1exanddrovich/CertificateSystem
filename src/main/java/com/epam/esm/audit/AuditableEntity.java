package com.epam.esm.audit;

import com.epam.esm.converter.ZonedDateTimeAttributeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "audit")
public class AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "entity_id")
    private long entityId;
    @Column(name = "operation_type")
    private String operationType;
    @Column(name = "entity_type")
    private String entityType;
    @Column(name = "operation_timestamp")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime operationTimeStamp;

    public AuditableEntity() { }

    public AuditableEntity(long entityId, String entityType, String operationType) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.operationType = operationType;
    }

    public long getId() {
        return id;
    }

    public long getEntityId() {
        return entityId;
    }

    public String getOperationType() {
        return operationType;
    }

    public ZonedDateTime getOperationTimeStamp() {
        return operationTimeStamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setOperationTimeStamp(ZonedDateTime operationTimeStamp) {
        this.operationTimeStamp = operationTimeStamp;
    }


    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
