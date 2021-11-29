package com.epam.esm.dtomapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.utils.Constants;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
        return new GiftCertificate(
                giftCertificateDto.getId(),
                giftCertificateDto.getName(),
                giftCertificateDto.getDescription(),
                giftCertificateDto.getPrice(),
                giftCertificateDto.getDuration() == 0 ? null
                        : Duration.ofDays(giftCertificateDto.getDuration()),
                giftCertificateDto.getCreationDate() == null ? null
                        : LocalDateTime.parse(giftCertificateDto.getCreationDate(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)),
                giftCertificateDto.getLastUpdateDate() == null ? null
                        : LocalDateTime.parse(giftCertificateDto.getLastUpdateDate(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)),
                giftCertificateDto.getTags().stream().map(Tag::new).collect(Collectors.toSet()));
    }

}
