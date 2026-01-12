package com.ranran.generator.service;

import com.ranran.generator.domain.dto.ClassInfoDto;
import com.ranran.generator.domain.entity.GeneratorTableField;
import com.ranran.generator.utils.GenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ranran
 */

@Service
public class GeneratorService {
    @Autowired
    private GeneratorTableFieldService generatorTableFieldService;
    @Autowired
    private FreemarkerService freemarkerService;

    public Map<String, String> generateCode(ClassInfoDto classInfoDto) {
        List<GeneratorTableField> generatorTableFields = generatorTableFieldService
                .fetchTableFields(classInfoDto.getTableName());
        Map<String, String> codeMap = new HashMap<>();
        GenerateUtil.buildClassInfo(classInfoDto, generatorTableFields);
        codeMap.put("entity.java", generateEntity(classInfoDto));
        codeMap.put("mapper.java", generateMapper(classInfoDto));
        codeMap.put("service.java", generateService(classInfoDto));
        codeMap.put("controller.java", generateController(classInfoDto));
        return codeMap;
    }

    public String generateEntity(ClassInfoDto classInfoDto) {
        return freemarkerService.generateString("java/entity.ftl", classInfoDto);
    }

    public String generateMapper(ClassInfoDto classInfoDto) {
        return freemarkerService.generateString("java/mapper.ftl", classInfoDto);
    }

    public String generateService(ClassInfoDto classInfoDto) {
        return freemarkerService.generateString("java/service.ftl", classInfoDto);
    }

    public String generateController(ClassInfoDto classInfoDto) {
        return freemarkerService.generateString("java/controller.ftl", classInfoDto);
    }

}
