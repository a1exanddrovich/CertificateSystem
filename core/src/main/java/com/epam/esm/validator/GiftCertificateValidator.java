package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

@Component
public class GiftCertificateValidator {

    private final TagValidator tagValidator;

    @Autowired
    public GiftCertificateValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    public boolean validateCreating(GiftCertificate giftCertificate) {
        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        BigDecimal price = giftCertificate.getPrice();
        Duration duration = giftCertificate.getDuration();
        Set<Tag> tags = giftCertificate.getTags();

        return (name != null && (name.length() >= 3 && name.length() <= 50)) &&
               (description != null && (description.length() >= 10 && description.length() <= 100)) &&
               (price != null && (price.compareTo(BigDecimal.ZERO) >= 1)) &&
               (duration != null && (duration.toDays() > 0)) &&
               (tags != null && tags.stream().allMatch(tagValidator::validate));
    }

    public boolean validateUpdating(GiftCertificate giftCertificate) {
        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        BigDecimal price = giftCertificate.getPrice();
        Duration duration = giftCertificate.getDuration();
        Set<Tag> tags = giftCertificate.getTags();

        return (name == null || name.length() >= 3 && name.length() <= 50) &&
               (description == null || description.length() >= 10 && description.length() <= 100) &&
               (price == null || price.compareTo(BigDecimal.ZERO) >= 1) &&
               (duration == null || duration.toDays() > 0) &&
               (tags == null || tags.stream().allMatch(tagValidator::validate));

    }

}
