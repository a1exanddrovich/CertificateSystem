package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;
import java.math.BigDecimal;
import java.util.Objects;

public class OrderDto extends RepresentationModel<OrderDto> {

    private long id;
    private GiftCertificateDto giftCertificate;
    private String timeStamp;
    private BigDecimal price;

    public OrderDto() { }

    public OrderDto(long id, GiftCertificateDto giftCertificateDto, String timeStamp, BigDecimal price) {
        this.id = id;
        this.giftCertificate = giftCertificateDto;
        this.timeStamp = timeStamp;
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGiftCertificate(GiftCertificateDto giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public GiftCertificateDto getGiftCertificate() {
        return giftCertificate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        OrderDto orderDto = (OrderDto) o;

        return id == orderDto.id && Objects.equals(giftCertificate, orderDto.giftCertificate) && Objects.equals(timeStamp, orderDto.timeStamp) && Objects.equals(price, orderDto.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, giftCertificate, timeStamp, price);
    }
}
