package com.ranran.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranran.common.domain.entity.SysMenu;
import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.SecurityUtil;
import com.ranran.persistence.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author ranran
 * 菜单Service
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {

    public boolean canAdd(SysMenu sysMenu) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getPath, sysMenu.getPath())
                .last("limit 1");
        SysMenu dbMenu = getOne(queryWrapper);
        return ObjUtil.isNull(dbMenu);
    }

    public boolean canUpdate(SysMenu sysMenu) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getPath, sysMenu.getPath())
                .last("limit 1");
        SysMenu dbMenu = getOne(queryWrapper);
        if (ObjUtil.isNull(dbMenu)) {
            return true;
        } else if (dbMenu.getMenuId().equals(sysMenu.getMenuId())) {
            return true;
        }
        return false;
    }

    public Set<String> getPermissionsByRole(Set<Long> roleIdSet) {
        return baseMapper.selectPermissionsByRole(roleIdSet);
    }

    public List<SysMenu> listAllMenu(SysMenu sysMenu) {
        return baseMapper.selectAllMenu(sysMenu);
    }

    public List<SysMenu> getRoutes() {
        if (SecurityUtil.isAdmin(SecurityUtil.getLoginUser().getUserId())) {
            return baseMapper.selectRoutes();
        }
        return baseMapper.selectRoutesByUserId(SecurityUtil.getLoginUser().getUserId());
    }

    public boolean hasChildren(Long menuId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId, menuId);
        return baseMapper.selectCount(queryWrapper) > 0;
    }

    // 递归构建树形结构
    public List<SysMenu> buildTree(List<SysMenu> menus, Long parentId) {
        List<SysMenu> treeMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                // 递归查找子菜单
                List<SysMenu> children = buildTree(menus, menu.getMenuId());
                menu.setChildren(children);
                treeMenus.add(menu);
            }
        }
        // 按排序号升序
        treeMenus.sort(Comparator.comparingInt(SysMenu::getSort));
        return treeMenus;
    }

}
