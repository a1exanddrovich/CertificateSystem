package com.epam.esm.utils;

public class InitialOrderDataHolder {

    private long userId;
    private long giftCertificateId;

    public InitialOrderDataHolder(long userId, long giftCertificateId) {
        this.userId = userId;
        this.giftCertificateId = giftCertificateId;
    }

    public InitialOrderDataHolder() { }

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
