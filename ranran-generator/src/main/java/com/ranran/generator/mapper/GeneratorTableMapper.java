package com.ranran.generator.mapper;

import com.ranran.generator.domain.entity.GeneratorTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ranran
 */
public interface GeneratorTableMapper {
    List<GeneratorTable> selectAllTable(@Param("dbName") String dbName);
}
