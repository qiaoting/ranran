package com.ranran.common.domain.enums;

/**
 * @author ranran
 * 状态枚举
 */
public enum ConfigTypeEnum {

    SYSTEM("1", "系统配置"),

    USER("2", "普通配置");

    private final String code;

    private final String desc;

    ConfigTypeEnum(String code, String desc) {
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