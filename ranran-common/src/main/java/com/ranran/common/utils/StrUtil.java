package com.ranran.common.utils;

import org.springframework.util.StringUtils;

/**
 * @author ranran
 * 字符串工具封装
 */
public class StrUtil {

    public static boolean hasLength(CharSequence str) {
        return StringUtils.hasLength(str);
    }

    public static boolean hasLength(String str) {
        return StringUtils.hasLength(str);
    }

    public static boolean hasText(CharSequence str) {
        return StringUtils.hasText(str);
    }

    public static boolean hasText(String str) {
        return StringUtils.hasText(str);
    }

    public static String trim(String str) {
        return (null == str ? "" : str.trim());
    }

    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

}
