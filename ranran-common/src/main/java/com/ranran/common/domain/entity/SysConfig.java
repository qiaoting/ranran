package com.ranran.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.ranran.common.constant.DateConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 系统配置表实体类
 * @author ranran
 */
@Data
@TableName("sys_config")
public class SysConfig {

    @TableId(type = IdType.AUTO)
    private Integer configId;

    private String configName;

    private String configKey;

    private String configValue;

    private String configType;

    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;

    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;


}