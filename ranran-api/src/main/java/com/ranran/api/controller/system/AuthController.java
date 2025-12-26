package com.ranran.api.controller.system;

import com.ranran.common.domain.Result;
import com.ranran.common.domain.dto.LoginDto;
import com.ranran.common.domain.entity.SysUser;
import com.ranran.common.domain.vo.LoginUser;
import com.ranran.common.utils.MessageUtil;
import com.ranran.common.utils.SecurityUtil;
import com.ranran.framework.web.controller.BaseController;
import com.ranran.framework.web.service.JwtService;
import com.ranran.framework.web.service.AuthService;
import com.ranran.persistence.service.impl.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ranran
 * 认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<Map> login(HttpServletRequest request, HttpServletResponse response,
                             @RequestBody LoginDto loginDto) {
        return authService.validate(loginDto);
    }

    @GetMapping("/getInfo")
    public Result<Map<String, Object>> getInfo(HttpServletRequest request) {
        UserDetails userDetails = (UserDetails) SecurityUtil.getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getUserByUsername(userDetails.getUsername());
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", sysUser.getUserId());
        userMap.put("username", sysUser.getUsername());
        userMap.put("nickname", sysUser.getNickname());
        userMap.put("avatar", sysUser.getAvatar());
        return Result.success(userMap);
    }

    @GetMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        LoginUser loginUser = jwtService.getLoginUser(request);
        jwtService.logout(loginUser.getUuid());
        return Result.success(MessageUtil.getI18nMsg("logout.success"));
    }

}