package com.ranran.generator.utils;

import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.StrUtil;
import org.springframework.util.StringUtils;

/**
 * @author ranran
 */
public class NameUtil {
    public static String toBigCamelCase(String name, String[] removePrefixes) {
        if (!StrUtil.hasText(name)) {
            throw new IllegalArgumentException("名称不能为空");
        }
        String lowerTableName = name.toLowerCase();
        if (ObjUtil.isNotNull(removePrefixes)) {
            for (String prefix : removePrefixes) {
                if (lowerTableName.startsWith(prefix.toLowerCase())) {
                    lowerTableName = lowerTableName.substring(prefix.length());
                    break;
                }
            }
        }
        String[] words = lowerTableName.split("_");
        StringBuilder className = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                className.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
        }
        if (className.isEmpty()) {
            throw new RuntimeException("转换后的类名不能为空，表名：" + name);
        }
        return className.toString();
    }
    public static String toSmallCamelCase(String name) {
        String bigCamelCase = toBigCamelCase(name, null);
        return StringUtils.uncapitalize(bigCamelCase);
    }

}
