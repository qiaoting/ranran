package com.ranran.api.controller.system;

import com.ranran.common.domain.Result;
import com.ranran.common.domain.entity.SysMenu;
import com.ranran.common.domain.vo.RouteVo;
import com.ranran.common.utils.StrUtil;
import com.ranran.persistence.service.impl.SysMenuService;
import com.ranran.persistence.service.impl.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author ranran
 * 菜单接口
 */
@RestController
@RequestMapping("/api/menu")
@Validated
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 获取菜单树（用于菜单的列表页面）
     */
    @GetMapping("/getAllMenuTree")
    @PreAuthorize("@pcs.hasPermission('system:menu:index')")
    public Result<List<SysMenu>> getAllMenuTree(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuService.listAllMenu(sysMenu);
        List<SysMenu> menuTree = sysMenuService.buildTree(list, 0L);
        return Result.success(menuTree);
    }

    /**
     * 获取菜单树（用于前端路由渲染）
     */
    @GetMapping("/getRoutesTree")
    public Result<List<RouteVo>> getRoutesTree(SysMenu sysMenu) {
        List<RouteVo> list = sysMenuService.getRoutes();
        List<RouteVo> menuTree = sysMenuService.buildRouteTree(list, 0L);
        return Result.success(menuTree);
    }

    /**
     * 根据ID获取菜单详情
     */
    @GetMapping("/getById")
    @PreAuthorize("@pcs.hasPermission('system:menu:index')")
    public Result<SysMenu> getById(@RequestParam Long menuId) {
        SysMenu menu = sysMenuService.getById(menuId);
        return Result.success(menu);
    }

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    @PreAuthorize("@pcs.hasPermission('system:menu:index')")
    public Result saveMenu(@Validated @RequestBody SysMenu sysMenu) {
        if(!sysMenuService.canAdd(sysMenu)) {
            return Result.fail("新增菜单【" + sysMenu.getMenuName() + "】失败，菜单编码已存在");
        }
        sysMenu.setMenuCode(StrUtil.capitalize(sysMenu.getPath()));
        sysMenuService.save(sysMenu);
        return Result.success();
    }

    /**
     * 修改菜单
     */
    @PostMapping("/edit")
    @PreAuthorize("@pcs.hasPermission('system:menu:index')")
    public Result updateMenu(@RequestBody SysMenu sysMenu) {
        if(!sysMenuService.canUpdate(sysMenu)) {
            return Result.fail("修改菜单【" + sysMenu.getMenuName() + "】失败，菜单编码已存在");
        }
        sysMenu.setMenuCode(StrUtil.capitalize(sysMenu.getPath()));
        sysMenuService.updateById(sysMenu);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/delete")
    @PreAuthorize("@pcs.hasPermission('system:menu:index')")
    public Result deleteMenu(@RequestParam Long menuId) {
        boolean hasChildren = sysMenuService.hasChildren(menuId);
        if (hasChildren) {
            return Result.fail("存在子菜单，无法删除");
        } else if (sysRoleMenuService.isMenuInUse(menuId)) {
            return Result.fail("菜单已在角色中分配，无法删除");
        }
        sysMenuService.removeById(menuId);
        return Result.success();
    }
}