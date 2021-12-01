package com.epam.esm.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static LocalDateTime now(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String nowOfPattern = formatter.format(now);
        return LocalDateTime.parse(nowOfPattern, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
    }

}
