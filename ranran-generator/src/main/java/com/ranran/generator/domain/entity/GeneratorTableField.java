package com.ranran.generator.domain.entity;

import lombok.Data;

/**
 * @author ranran
 */
@Data
public class GeneratorTableField {
    /** 字段名 */
    private String columnName;
    /** 字段类型，数据库原生类型 */
    private String columnType;
    /** java中属性类型 */
    private String javaType;
    /** 字段注释 */
    private String remarks;
    /** 是否为主键 */
    private boolean isPrimaryKey;
    /** 是否可为空 */
    private boolean isNullable;
}
