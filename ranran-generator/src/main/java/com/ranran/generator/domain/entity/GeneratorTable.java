package com.ranran.generator.domain.entity;

import lombok.Data;

/**
 * @author ranran
 */
@Data
public class GeneratorTable {
    private String tableName;
    private String comment;
    private String engine;
}
