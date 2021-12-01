package com.epam.esm.converter;

import com.epam.esm.utils.Constants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }

}
