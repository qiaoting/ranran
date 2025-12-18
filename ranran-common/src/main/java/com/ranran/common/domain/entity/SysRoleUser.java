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
 * 角色用户实体类
 */
@Data
@TableName("sys_role_user")
public class SysRoleUser {

    @TableId(type = IdType.AUTO)
    private Long userRoleId;

    private Long userId;

    private Long roleId;

    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;
}
