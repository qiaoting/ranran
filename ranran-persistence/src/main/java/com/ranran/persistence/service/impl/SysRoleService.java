package com.ranran.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranran.common.domain.entity.SysMenu;
import com.ranran.common.domain.entity.SysRole;
import com.ranran.common.utils.ObjUtil;
import com.ranran.persistence.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;

/**
 * @author ranran
 * 角色Service
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    public boolean canAdd(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, sysRole.getRoleCode())
                .last("limit 1");
        SysRole dbRole = getOne(queryWrapper);
        return ObjUtil.isNull(dbRole);
    }

    public boolean canUpdate(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, sysRole.getRoleCode())
                .last("limit 1");
        SysRole dbRole = getOne(queryWrapper);
        if (ObjUtil.isNull(dbRole)) {
            return true;
        } else if (dbRole.getRoleId().equals(sysRole.getRoleId())) {
            return true;
        }
        return false;
    }

}
