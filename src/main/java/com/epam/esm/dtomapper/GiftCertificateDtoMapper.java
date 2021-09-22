package com.epam.esm.dtomapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Component
public class GiftCertificateDtoMapper {

    public GiftCertificateDto map(GiftCertificate giftCertificate) {
        return new GiftCertificateDto(giftCertificate.getId(),
                                      giftCertificate.getName(),
                                      giftCertificate.getDescription(),
                                      giftCertificate.getPrice(),
                                      (int) giftCertificate.getDuration().toDays(),
                                      giftCertificate.getCreationDate().toString(),
                                      giftCertificate.getLastUpdateDate().toString(),
                                      giftCertificate.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
    }

    public GiftCertificate unmap(GiftCertificateDto giftCertificateDto) {
        return new GiftCertificate(giftCertificateDto.getId(),
                                   giftCertificateDto.getName(),
                                   giftCertificateDto.getDescription(),
                                   giftCertificateDto.getPrice(),
                                   giftCertificateDto.getDuration() == 0 ? null
                                           : Duration.ofDays(giftCertificateDto.getDuration()),
                                   giftCertificateDto.getCreationDate() == null ? null
                                           : ZonedDateTime.parse(giftCertificateDto.getCreationDate()),
                                   giftCertificateDto.getLastUpdateDate() == null ? null
                                           : ZonedDateTime.parse(giftCertificateDto.getLastUpdateDate()),
                                   giftCertificateDto.getTags().stream().map(Tag::new).collect(Collectors.toSet()) );
    }

}
