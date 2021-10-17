package com.epam.esm.auditTest;

import com.epam.esm.converter.ZonedDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "order_audit")
@EntityListeners(AuditOrderListener.class)
public class AuditableOrderTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "entity_id")
    private long entityId;
    @Column(name = "operation_type")
    private String operationType;
    @Column(name = "timestamp")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime operationTimeStamp;

    public AuditableOrderTest() { }

    public AuditableOrderTest(long id, long entityId, String operationType, ZonedDateTime operationTimeStamp) {
        this.id = id;
        this.entityId = entityId;
        this.operationType = operationType;
        this.operationTimeStamp = operationTimeStamp;
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
