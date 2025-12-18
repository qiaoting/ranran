package com.ranran.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ranran.common.constant.DateConstant;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ranran
 * 登录日志实体类
 */
@Data
@TableName("sys_login_log")
public class SysLoginLog {
    @TableId(type = IdType.AUTO)
    private Long logId;

    private String username;

    private String msg;

    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;
}
