package com.ranran.generator.utils;

import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.StrUtil;

/**
 * @author ranran
 */
public class TableUtil {
    public static String convert(String tableName, String[] removePrefixes) {
        if (!StrUtil.hasText(tableName)) {
            throw new IllegalArgumentException("表名不能为空");
        }
        String lowerTableName = tableName.toLowerCase();
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
            throw new RuntimeException("转换后的类名不能为空，表名：" + tableName);
        }
        return className.toString();
    }

    public static String convert(String tableName) {
        return convert(tableName, null);
    }

}
