package com.ranran.generator.utils;

import com.ranran.common.utils.StrUtil;
import com.ranran.generator.common.GenerateConstant;
import com.ranran.generator.domain.dto.ClassInfoDto;
import com.ranran.generator.domain.entity.GeneratorTableField;

import java.util.List;

/**
 * @author ranran
 */
public class GenerateUtil {

    public static ClassInfoDto buildClassInfo(ClassInfoDto classInfoDto, List<GeneratorTableField> generatorTableFields) {
        if (!StrUtil.hasText(classInfoDto.getModuleName())) {
            classInfoDto.setModuleName(GenerateConstant.MODULE_PACKAGE);
        }
        classInfoDto.setFields(generatorTableFields);
        classInfoDto.setBasePackageName(GenerateConstant.BASE_PACKAGE);
        classInfoDto.setEntityClassName(NameUtil.toBigCamelCase(classInfoDto.getTableName(), null));
        for (GeneratorTableField tableField : generatorTableFields) {
            tableField.setPropertyName(NameUtil.toSmallCamelCase(tableField.getColumnName()));
        }
        return classInfoDto;
    }

}
