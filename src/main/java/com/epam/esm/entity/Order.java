package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;
import com.epam.esm.auditTest.AuditOrderListener;
import com.epam.esm.converter.ZonedDateTimeAttributeConverter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "`order`")
@EntityListeners(AuditOrderListener.class)
public class Order implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;
    @Column(name = "timestamp")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime timeStamp;
    @Column(name = "price", scale = 2, precision = 10)
    private BigDecimal price;

    public Order(long id, User user, GiftCertificate giftCertificate, ZonedDateTime timeStamp, BigDecimal price) {
        this.id = id;
        this.user = user;
        this.giftCertificate = giftCertificate;
        this.timeStamp = timeStamp;
        this.price = price;
    }

    public Order() { }

    @Override
    public long getId() {
        return this.id;
    }

    public ZonedDateTime getTimeStamp() {
        return this.timeStamp;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;
        return Objects.equals(id, order.id)
                && timeStamp.equals(order.timeStamp)
                && price.equals(order.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeStamp, price);
    }

    @PrePersist
    public void onPrePersist() {
        System.out.println("Persist");
    }

    @PreUpdate
    public void onPreUpdate() {
        System.out.println("Update");
    }

    @PreRemove
    public void onPreRemove() {
        System.out.println("Remove");
    }

}
