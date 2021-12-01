package com.epam.esm.dto;

public class OrderRequestDto {

    private long userId;
    private long giftCertificateId;

    public OrderRequestDto(long userId, long giftCertificateId) {
        this.userId = userId;
        this.giftCertificateId = giftCertificateId;
    }

    public OrderRequestDto() { }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setGiftCertificateId(long giftCertificateId) {
        this.giftCertificateId = giftCertificateId;
    }

    public long getUserId() {
        return userId;
    }

    public long getGiftCertificateId() {
        return giftCertificateId;
    }
}
