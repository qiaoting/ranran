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
 * 角色菜单实体类
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu {

    @TableId(type = IdType.AUTO)
    private Long roleMenuId;

    private Long roleId;

    private Long menuId;

    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;
}
