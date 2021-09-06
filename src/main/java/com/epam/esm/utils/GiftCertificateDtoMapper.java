package com.epam.esm.utils;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.ZonedDateTime;

@Component
public class GiftCertificateDtoMapper {

    public GiftCertificateDto map(GiftCertificate giftCertificate) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate.getId(),
                                                                       giftCertificate.getName(),
                                                                       giftCertificate.getDescription(),
                                                                       giftCertificate.getPrice(),
                                                                       (int) giftCertificate.getDuration().toDays(),
                                                                       giftCertificate.getCreationDate().toString(),
                                                                       giftCertificate.getLastUpdateDate().toString());

        giftCertificate.getTags().stream().forEach(tag -> giftCertificateDto.addTag(tag.getName()));
        return giftCertificateDto;
    }

    public GiftCertificate unmap(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = new GiftCertificate(giftCertificateDto.getId(),
                                                              giftCertificateDto.getName(),
                                                              giftCertificateDto.getDescription(),
                                                              giftCertificateDto.getPrice(),
                                                              giftCertificateDto.getDuration() == 0 ? null
                                                                                                    : Duration.ofDays(giftCertificateDto.getDuration()),
                                                              giftCertificateDto.getCreationDate() == null ? null
                                                                                                           : ZonedDateTime.parse(giftCertificateDto.getCreationDate()),
                                                              giftCertificateDto.getLastUpdateDate() == null ? null
                                                                                                             : ZonedDateTime.parse(giftCertificateDto.getLastUpdateDate()));

        giftCertificateDto.getTags().stream().forEach(tagName -> giftCertificate.addTag(new Tag(tagName)));
        return giftCertificate;
    }

}
