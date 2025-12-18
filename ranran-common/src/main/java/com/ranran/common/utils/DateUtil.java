package com.ranran.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ranran.common.constant.DateConstant.YYYY_MM_DD_HH_MM_SS;

/**
 * @author ranran
 * 日期工具封装
 */
public class DateUtil {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);

    public static String format(LocalDate date, String pattern) {
        if (StrUtil.hasText(pattern)) {
            return date.format(DateTimeFormatter.ofPattern(pattern));
        } else {
            return date.format(DATE_TIME_FORMATTER);
        }
    }

    public static String now() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    public static String yesterday() {
        return LocalDateTime.now().minusDays(1).format(DATE_TIME_FORMATTER);
    }

}
