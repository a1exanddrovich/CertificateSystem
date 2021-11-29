package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;
import com.epam.esm.converter.LocalDateTimeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "`order`")
@EntityListeners(AuditListener.class)
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
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime timeStamp;
    @Column(name = "price", scale = 2, precision = 10)
    private BigDecimal price;

    public Order(long id, User user, GiftCertificate giftCertificate, LocalDateTime timeStamp, BigDecimal price) {
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

    @Override
    public String getType() {
        return "Order";
    }

    public LocalDateTime getTimeStamp() {
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

    public void setTimeStamp(LocalDateTime timeStamp) {
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

}
