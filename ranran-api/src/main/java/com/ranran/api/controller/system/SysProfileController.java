package com.ranran.api.controller.system;

import com.ranran.common.domain.Result;
import com.ranran.common.domain.dto.PasswordDto;
import com.ranran.common.domain.entity.SysUser;
import com.ranran.common.domain.enums.StatusEnum;
import com.ranran.common.utils.SecurityUtil;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.web.controller.BaseController;
import com.ranran.persistence.service.impl.SysRoleUserService;
import com.ranran.persistence.service.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ranran
 * 个人中心接口
 */
@RestController
@RequestMapping("/api/profile")
@Validated
public class SysProfileController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    @GetMapping("/getSelf")
    public Result<SysUser> getSelf(@RequestParam Long userId) {
        SysUser dbUser = sysUserService.getUserById(userId);
        dbUser.setAdmin(SecurityUtil.isAdmin(userId));
        return Result.success(dbUser);
    }

    @PostMapping("/updatePassword")
    public Result<Void> updatePassword(@Validated @RequestBody PasswordDto passwordDto) {
        SysUser dbUser = sysUserService.getById(passwordDto.getUserId());
        if (!SecurityUtil.passwordMatch(passwordDto.getOldPassword(), dbUser.getPassword())) {
            return Result.fail("原密码不对");
        }
        dbUser.setPassword(SecurityUtil.bcryptEncode(passwordDto.getNewPassword()));
        boolean save = sysUserService.updateById(dbUser);
        if (save) {
            return Result.success();
        } else {
            return Result.fail("修改失败");
        }
    }

    @PostMapping("/updateSelf")
    public Result<Void> updateSelf(@Validated @RequestBody SysUser user) {
        if (SecurityUtil.isAdmin(user.getUserId())) {
            if (StrUtil.hasText(user.getStatus()) &&
                    user.getStatus().equals(StatusEnum.DISABLE.getCode()))
                return Result.fail("禁止禁用管理员");
        }
        user.setPassword(null);
        boolean save = sysUserService.updateById(user);
        if (save) {
            return Result.success();
        } else {
            return Result.fail("修改失败");
        }
    }

}