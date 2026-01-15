package ${basePackageName}.common.domain.entity;
<#assign importTypes = []>

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import com.ranran.common.constant.DateConstant;
<#list fields as field>
    <#if field.javaType == "LocalDateTime">
        <#if !importTypes?seq_contains("LocalDateTime")>
            <#assign importTypes = importTypes + ["LocalDateTime"]>
        </#if>
    <#elseif field.javaType == "BigDecimal">
        <#if !importTypes?seq_contains("BigDecimal")>
            <#assign importTypes = importTypes + ["BigDecimal"]>
        </#if>
    </#if>
</#list>
<#list importTypes as type>
    <#if type == "LocalDateTime">
import java.time.LocalDateTime;
    <#elseif type == "BigDecimal">
import java.math.BigDecimal;
    </#if>
</#list>

/**
* ${functionName}实体类
* @author ${author}
*/
@Data
@TableName("${tableName}")
public class ${entityClassName} {

<#list fields as field>
    <#if field.isPrimaryKey()>
    @TableId(type = IdType.AUTO)
    </#if>
    <#if field.javaType == "LocalDateTime">
    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>

}