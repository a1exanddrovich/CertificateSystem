package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OrderDto extends RepresentationModel<OrderDto> {

    private long id;
    private UserDto user;
    private GiftCertificateDto giftCertificate;
    private ZonedDateTime timeStamp;
    private BigDecimal price;

    public OrderDto() { }

    public OrderDto(long id, UserDto user, GiftCertificateDto giftCertificate, ZonedDateTime timeStamp, BigDecimal price) {
        this.id = id;
        this.user = user;
        this.giftCertificate = giftCertificate;
        this.timeStamp = timeStamp;
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setGiftCertificate(GiftCertificateDto giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public UserDto getUser() {
        return user;
    }

    public GiftCertificateDto getGiftCertificate() {
        return giftCertificate;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderDto orderDto = (OrderDto) o;
        return id == orderDto.id && Objects.equals(user, orderDto.user) && Objects.equals(giftCertificate, orderDto.giftCertificate) && Objects.equals(timeStamp, orderDto.timeStamp) && Objects.equals(price, orderDto.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, user, giftCertificate, timeStamp, price);
    }

}
