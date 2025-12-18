package com.ranran.api.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.common.domain.Result;
import com.ranran.common.domain.dto.RoleUserDto;
import com.ranran.common.domain.dto.RoleUserQueryDto;
import com.ranran.common.domain.entity.SysUser;
import com.ranran.common.domain.vo.PageVo;
import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.context.PageContext;
import com.ranran.framework.web.controller.BaseController;
import com.ranran.persistence.service.impl.SysRoleUserService;
import com.ranran.persistence.service.impl.SysUserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ranran
 * 角色用户绑定相关接口
 */
@RestController
@RequestMapping("/api/roleUser")
@Validated
public class SysRoleUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    @GetMapping("/page")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public PageVo page(RoleUserQueryDto dto) {
        Page<SysUser> page = PageContext.getPage();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        List<Long> userIds = sysRoleUserService.selectUserListByRoleId(dto.getRoleId());
        if (userIds.isEmpty()) {
            return getPageData(List.of(), 0L);
        }
        queryWrapper.in(SysUser::getUserId, userIds);
        if (StrUtil.hasText(dto.getUsername())) {
            queryWrapper.like(SysUser::getUsername, dto.getUsername());
        }
        if (StrUtil.hasText(dto.getNickname())) {
            queryWrapper.like(SysUser::getNickname, dto.getNickname());
        }
        IPage<SysUser> userPage = sysUserService.page(page, queryWrapper);
        userPage.getRecords().forEach(user -> user.setPassword(null));
        return getPageData(userPage.getRecords(), userPage.getTotal());
    }

    @DeleteMapping("/remove")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public Result<Void> remove(RoleUserDto dto) {
        boolean success = sysRoleUserService.deleteUserRole(dto.getRoleId(), dto.getUserIds());
        return success ? Result.success() : Result.fail("移除用户失败");
    }

    @PostMapping("/appendUsers")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public Result<Void> appendUsers(@Validated @RequestBody RoleUserDto dto) {
        boolean success = sysRoleUserService.appendUsers(dto.getRoleId(), dto.getUserIds());
        return success ? Result.success() : Result.fail("用户分配失败");
    }

    @GetMapping("/getRoleUsers")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public Result<List<Long>> getRoleUsers(@RequestParam @NotNull Long roleId) {
        if (ObjUtil.isNull(roleId)) {
            return Result.fail("角色ID不能为空");
        }
        List<Long> userIds = sysRoleUserService.selectUserListByRoleId(roleId);
        return Result.success(userIds);
    }

    @GetMapping("/unallocatedPage")
    @PreAuthorize("@pcs.hasPermission('system:role:index')")
    public PageVo unallocatedPage(Long roleId) {
        Page<SysUser> page = PageContext.getPage();
        IPage<SysUser> userPage = sysRoleUserService.getUnallocatedPage(page, roleId);
        userPage.getRecords().forEach(temp -> temp.setPassword(null));
        return getPageData(userPage.getRecords(), userPage.getTotal());
    }

}