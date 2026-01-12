package com.ranran.generator.mapper;

import com.ranran.generator.domain.entity.GeneratorTableField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ranran
 */
public interface GeneratorTableFieldMapper{

    List<GeneratorTableField> selectAllField(@Param("tableName") String tableName);
}
