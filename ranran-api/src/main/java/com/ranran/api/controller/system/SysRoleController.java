package com.ranran.api.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.common.domain.Result;
import com.ranran.common.domain.entity.SysRole;
import com.ranran.common.domain.entity.SysRoleMenu;
import com.ranran.common.domain.vo.PageVo;
import com.ranran.common.utils.CollUtil;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.context.PageContext;
import com.ranran.framework.web.controller.BaseController;
import com.ranran.persistence.service.impl.SysRoleMenuService;
import com.ranran.persistence.service.impl.SysRoleService;
import com.ranran.persistence.service.impl.SysRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ranran
 * 角色接口
 */
@RestController
@RequestMapping("/api/role")
@Validated
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    @GetMapping("/page")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public PageVo page(SysRole sysRole) {
        Page<SysRole> page = PageContext.getPage();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.hasText(sysRole.getRoleName())) {
            queryWrapper.like(SysRole::getRoleName, sysRole.getRoleName());
        }
        if (StrUtil.hasText(sysRole.getRoleCode())) {
            queryWrapper.like(SysRole::getRoleCode, sysRole.getRoleCode());
        }
        if (StrUtil.hasText(sysRole.getStatus())) {
            queryWrapper.eq(SysRole::getStatus, sysRole.getStatus());
        }
        IPage<SysRole> rolePage = sysRoleService.page(page, queryWrapper);
        return getPageData(rolePage.getRecords(), rolePage.getTotal());
    }

    @PostMapping("/add")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public Result<Void> add(@Validated @RequestBody SysRole role) {
        if (!sysRoleService.canAdd(role)) {
            return Result.fail("新增角色【" + role.getRoleName() + "】失败，角色编码已存在");
        }
        sysRoleService.save(role);
        if (CollUtil.isNotEmpty(role.getMenuIds())) {
            List<SysRoleMenu> rolePermissions = new ArrayList<>();
            for (Long menuId : role.getMenuIds()) {
                SysRoleMenu rolePermission = new SysRoleMenu();
                rolePermission.setRoleId(role.getRoleId());
                rolePermission.setMenuId(menuId);
                rolePermissions.add(rolePermission);
            }
            boolean saveBatch = sysRoleMenuService.saveBatch(rolePermissions);
            if (!saveBatch) {
                return Result.fail("保存角色菜单关联失败");
            }
        }
        return Result.success();
    }

    @PostMapping("/edit")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public Result<Void> edit(@Validated @RequestBody SysRole role) {
        if (!sysRoleService.canUpdate(role)) {
            return Result.fail("修改角色【" + role.getRoleName() + "】失败，角色编码已存在");
        }
        sysRoleService.updateById(role);
        LambdaQueryWrapper<SysRoleMenu> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysRoleMenu::getRoleId, role.getRoleId());
        sysRoleMenuService.remove(deleteWrapper);
        if (CollUtil.isNotEmpty(role.getMenuIds())) {
            List<SysRoleMenu> rolePermissions = new ArrayList<>();
            for (Long menuId : role.getMenuIds()) {
                SysRoleMenu rolePermission = new SysRoleMenu();
                rolePermission.setRoleId(role.getRoleId());
                rolePermission.setMenuId(menuId);
                rolePermissions.add(rolePermission);
            }
            boolean saveBatch = sysRoleMenuService.saveBatch(rolePermissions);
            if (!saveBatch) {
                return Result.fail("保存角色菜单关联失败");
            }
        }
        return Result.success();
    }

    @PostMapping("/changeStatus")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public Result<Void> changeStatus(@Validated @RequestBody SysRole role) {
        sysRoleService.updateById(role);
        return Result.success();
    }

    @GetMapping("/getById")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public Result<SysRole> getById(@RequestParam Long roleId) {
        SysRole dbRole = sysRoleService.getById(roleId);
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> rolePermissions = sysRoleMenuService.list(queryWrapper);
        List<Long> menuIds = rolePermissions.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
        dbRole.setMenuIds(menuIds);
        return Result.success(dbRole);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public Result<Void> delete(@RequestParam Long roleId) {
        if (sysRoleUserService.hasUsers(roleId)) {
            return Result.fail("角色中存在分配的用户，禁止删除");
        } else if (sysRoleMenuService.hasMenus(roleId)) {
            return Result.fail("角色中存在分配的菜单，禁止删除");
        }
        boolean remove = sysRoleService.removeById(roleId);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail("删除失败");
        }
    }

}