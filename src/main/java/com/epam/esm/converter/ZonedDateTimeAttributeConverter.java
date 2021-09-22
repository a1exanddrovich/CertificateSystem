package com.epam.esm.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, String> {

    @Override
    public String convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toString();
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(String s) {
        return ZonedDateTime.parse(s);
    }
}
