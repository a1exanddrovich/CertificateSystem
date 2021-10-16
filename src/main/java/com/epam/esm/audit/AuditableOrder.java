package com.epam.esm.audit;

import com.epam.esm.entity.Order;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "order_audit")
public class AuditableOrder extends AuditableEntity<Order> {

    public AuditableOrder(Order order) {
        super(order);
    }

    public AuditableOrder() { }

}
