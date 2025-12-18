package com.ranran.common.utils;

import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author ranran
 * 集合工具封装
 */
public class CollUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !CollectionUtils.isEmpty(collection);
    }
}
