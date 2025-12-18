package com.ranran.common.utils;

import java.util.UUID;

/**
 * @author ranran
 * ID工具封装
 */
public class IdUtil {

    public static String fastSimpleUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String fastSimpleUuidUpperCase() {
        return fastSimpleUuid().toUpperCase();
    }

}
