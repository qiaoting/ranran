package com.ranran.generator.common;

import java.util.Set;

/**
 * @author ranran
 */
public class GenerateConstant {

    public static final Set<String> STRING_TYPE = Set.of("char", "varchar", "varchar2", "nvarchar");
    public static final Set<String> LONG_TYPE = Set.of("bit", "bigint", "integer");
    public static final Set<String> INTEGER_TYPE = Set.of("int", "tinyint", "smallint", "mediumint");
    public static final Set<String> DATE_TIME_TYPE = Set.of("datetime", "time", "date", "timestamp");

    public static final String BASE_PACKAGE = "com.ranran";
    public static final String MODULE_PACKAGE = "system";

}
