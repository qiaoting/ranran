package com.ranran.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranran.common.domain.entity.SysRoleMenu;
import com.ranran.persistence.mapper.SysRoleMenuMapper;
import org.springframework.stereotype.Service;

/**
 * @author ranran
 * 角色菜单Service
 */
@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> {

    public boolean hasMenus(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        return exists(queryWrapper);
    }

    public boolean isMenuInUse(Long menuId) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getMenuId, menuId);
        return exists(queryWrapper);
    }

}
