package com.epam.esm.audit;

import com.epam.esm.converter.ZonedDateTimeAttributeConverter;
import com.epam.esm.entity.Identifiable;
import javax.persistence.*;
import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class AuditableEntity<T extends Identifiable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @Column(name = "entity_id")
    protected long entityId;
    @Column(name = "operation_type")
    protected String operationType;
    @Column(name = "timestamp")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    protected ZonedDateTime operationTimeStamp;

    public AuditableEntity() { }

    public AuditableEntity(T entity) {
       this.entityId = entity.getId();
    }

    public AuditableEntity(long entityId, String operationType, ZonedDateTime operationTimeStamp) {
        this.entityId = entityId;
        this.operationType = operationType;
        this.operationTimeStamp = operationTimeStamp;
    }

    public AuditableEntity(long id, Long entityId, String operationType, ZonedDateTime operationTimestamp) {
        this(entityId, operationType, operationTimestamp);
        this.id = id;
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



}
