package com.ranran.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author ranran
 */
public class MessageUtil {
    public static String getI18nMsg(String code, Object... args) {
        MessageSource messageSource = SpringContextUtil.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
