package com.epam.esm.entity;

import com.epam.esm.converter.ZonedDateTimeAttributeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity(name = "Order")
@Table(name = "orders")
@JsonIgnoreProperties({"user"})
public class Order implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;
    @Column(name = "timestamp")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime timeStamp;
    @Column(name = "price", scale = 2, precision = 10)
    private BigDecimal price;

    public Order(long id, ZonedDateTime timeStamp, BigDecimal price) {
        this.id = id;
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

}
