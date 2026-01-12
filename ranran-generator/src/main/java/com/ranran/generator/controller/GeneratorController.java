package com.ranran.generator.controller;

import com.ranran.common.domain.Result;
import com.ranran.generator.domain.dto.ClassInfoDto;
import com.ranran.generator.domain.entity.GeneratorTable;
import com.ranran.generator.service.GeneratorService;
import com.ranran.generator.service.GeneratorTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ranran
 */
@RestController
@RequestMapping("/api/generator")
public class GeneratorController {
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private GeneratorTableService generatorTableService;

    @GetMapping("/listTable")
    public Result<List<GeneratorTable>> listTable(@RequestParam(required = false) String dbName) {
        List<GeneratorTable> tables = generatorTableService.getAllTable(dbName);
        return Result.success(tables);
    }

    @PostMapping("/getCode")
    public Result<Map<String, String>> getCode(@RequestBody ClassInfoDto classInfoDto) {
        Map<String, String> codeMap = generatorService.generateCode(classInfoDto);
        return Result.success(codeMap);
    }

}
