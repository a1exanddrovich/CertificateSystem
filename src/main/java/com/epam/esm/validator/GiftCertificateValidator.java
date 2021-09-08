package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.Duration;

@Component
public class GiftCertificateValidator {

    private final TagValidator tagValidator;

    @Autowired
    public GiftCertificateValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    public boolean validateCreate(GiftCertificate giftCertificate) {
        return (giftCertificate.getName() != null && validateName(giftCertificate.getName())) &&
               (giftCertificate.getDescription() != null && validateDescription(giftCertificate.getDescription())) &&
               (giftCertificate.getPrice() != null && validatePrice(giftCertificate.getPrice())) &&
               (giftCertificate.getDuration() != null && validateDuration(giftCertificate.getDuration())) &&
               (giftCertificate.getTags() != null && giftCertificate.getTags().stream().allMatch(tagValidator::validate));
    }

    public boolean validateUpdate(GiftCertificate giftCertificate) {
        return (giftCertificate.getName() == null || validateName(giftCertificate.getName())) &&
               (giftCertificate.getDescription() == null || validateDescription(giftCertificate.getDescription())) &&
               (giftCertificate.getPrice() == null || validatePrice(giftCertificate.getPrice())) &&
               (giftCertificate.getDuration() == null || validateDuration(giftCertificate.getDuration())) &&
               (giftCertificate.getTags() == null || (giftCertificate.getTags().stream().allMatch(tagValidator::validate)));
    }

    private boolean validateName(String name) {
        return name.length() >= 3 && name.length() <= 50;
    }

    private boolean validateDescription(String description) {
        return description.length() >= 10 && description.length() <= 100;
    }

    private boolean validatePrice(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) >= 1;
    }

    private boolean validateDuration(Duration duration) {
        return duration.toDays() > 0;
    }

}
