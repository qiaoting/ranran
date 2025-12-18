package com.ranran.api.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.common.domain.Result;
import com.ranran.common.domain.entity.SysUser;
import com.ranran.common.domain.enums.StatusEnum;
import com.ranran.common.domain.vo.PageVo;
import com.ranran.common.utils.SecurityUtil;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.context.PageContext;
import com.ranran.framework.web.controller.BaseController;
import com.ranran.persistence.service.impl.SysRoleUserService;
import com.ranran.persistence.service.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ranran
 * 系统用户接口
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    @GetMapping("/page")
    @PreAuthorize("@pcs.hasPermission('system:user:index')")
    public PageVo page(SysUser user) {
        Page<SysUser> page = PageContext.getPage();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.hasText(user.getUsername())) {
            queryWrapper.like(SysUser::getUsername, user.getUsername());
        }
        if (StrUtil.hasText(user.getNickname())) {
            queryWrapper.like(SysUser::getNickname, user.getNickname());
        }
        if (StrUtil.hasText(user.getStatus())) {
            queryWrapper.eq(SysUser::getStatus, user.getStatus());
        }
        IPage<SysUser> userPage = sysUserService.page(page, queryWrapper);
        userPage.getRecords().forEach(temp -> temp.setPassword(null));
        return getPageData(userPage.getRecords(), userPage.getTotal());
    }

    @PostMapping("/add")
    @PreAuthorize("@pcs.hasPermission('system:user:index')")
    public Result<Void> add(@Validated @RequestBody SysUser user) {
        if (!sysUserService.canAdd(user)) {
            return Result.fail("新增用户【" + user.getUsername() + "】失败，用户名已存在");
        }
        user.setPassword(SecurityUtil.bcryptEncode(user.getPassword()));
        boolean save = sysUserService.save(user);
        if (save) {
            return Result.success();
        } else {
            return Result.fail("新增失败");
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("@pcs.hasPermission('system:user:index')")
    public Result<Void> edit(@Validated @RequestBody SysUser user) {
        if (!sysUserService.canUpdate(user)) {
            return Result.fail("修改用户【" + user.getUsername() + "】失败，用户名已存在");
        }
        if (SecurityUtil.isAdmin(user.getUserId())) {
            if (StrUtil.hasText(user.getStatus()) &&
                    user.getStatus().equals(StatusEnum.DISABLE.getCode()))
                return Result.fail("禁止禁用管理员");
        }
        user.setUsername(null);
        user.setPassword(null);
        boolean save = sysUserService.updateById(user);
        if (save) {
            return Result.success();
        } else {
            return Result.fail("修改失败");
        }
    }

    @GetMapping("/getById")
    @PreAuthorize("@pcs.hasPermission('system:user:index')")
    public Result<SysUser> getById(@RequestParam Long userId) {
        SysUser dbUser = sysUserService.getUserById(userId);
        dbUser.setAdmin(SecurityUtil.isAdmin(userId));
        return Result.success(dbUser);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@pcs.hasPermission('system:user:index')")
    public Result<Void> delete(@RequestParam Long userId) {
        if (SecurityUtil.isAdmin(userId)) {
            return Result.fail("禁止删除管理员");
        } else if (sysRoleUserService.isUserInUse(userId)) {
            return Result.fail("已在角色中分配，禁止删除");
        }
        boolean remove = sysUserService.removeById(userId);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail("删除失败");
        }
    }

}