package com.ranran.generator.domain.dto;

import com.ranran.generator.domain.entity.GeneratorTableField;
import lombok.Data;

import java.util.List;

/**
 * @author ranran
 */
@Data
public class ClassInfoDto {
    /** 基础信息 **/
    public String tableName;
    public String moduleName;
    public String functionName;
    public String author;
    public List<GeneratorTableField> fields;
    public String basePackageName;
    public String entityClassName;

}
