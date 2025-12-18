package com.ranran.common.domain.enums;

/**
 * @author ranran
 * 状态枚举
 */
public enum StatusEnum {

    DISABLE("0", "禁用"),

    ENABLE("1", "启用");

    private final String code;

    private final String desc;

    StatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}