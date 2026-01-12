package com.ranran.generator.service;

import com.ranran.generator.domain.entity.GeneratorTable;
import com.ranran.generator.mapper.GeneratorTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ranran
 */

@Service
public class GeneratorTableService {
    @Autowired
    private GeneratorTableMapper generatorTableMapper;

    public List<GeneratorTable> getAllTable(String dbName) {
        return generatorTableMapper.selectAllTable(dbName);
    }

}
