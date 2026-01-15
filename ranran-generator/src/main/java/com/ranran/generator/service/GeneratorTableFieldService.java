package com.ranran.generator.service;

import com.ranran.generator.common.GenerateConstant;
import com.ranran.generator.domain.entity.GeneratorTableField;
import com.ranran.generator.mapper.GeneratorTableFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ranran
 */

@Service
public class GeneratorTableFieldService {
    @Autowired
    private GeneratorTableFieldMapper generatorTableFieldMapper;

    public List<GeneratorTableField> fetchTableFields(String tableName) {
        List<GeneratorTableField> generatorTableFields = generatorTableFieldMapper.selectAllField(tableName);
        for (GeneratorTableField generatorTableField : generatorTableFields) {
            if (GenerateConstant.LONG_TYPE.contains(generatorTableField.getColumnType())) {
                generatorTableField.setPropertyType("Long");
            } else if (GenerateConstant.INTEGER_TYPE.contains(generatorTableField.getColumnType())) {
                generatorTableField.setPropertyType("Integer");
            } else if (GenerateConstant.DATE_TIME_TYPE.contains(generatorTableField.getColumnType())) {
                generatorTableField.setPropertyType("LocalDateTime");
            } else if (GenerateConstant.STRING_TYPE.contains(generatorTableField.getColumnType())) {
                generatorTableField.setPropertyType("String");
            } else {
                generatorTableField.setPropertyType("BigDecimal");
            }
        }
        return generatorTableFields;
    }

}
