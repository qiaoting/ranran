package com.ranran.framework.web.service;

import com.ranran.common.constant.Constant;
import com.ranran.common.domain.Result;
import com.ranran.common.domain.dto.LoginDto;
import com.ranran.common.domain.vo.LoginUser;
import com.ranran.common.utils.MessageUtil;
import com.ranran.common.utils.ObjUtil;
import com.ranran.persistence.service.impl.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ranran
 * 认证逻辑
 */
@Service
public class AuthService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private SysLoginLogService sysLoginLogService;

    public Result<Map> validate(LoginDto loginDto) {
        Map<String, String> data = new HashMap<>();
        Object realCode = redisTemplate.opsForValue().get(Constant.KAPTCHA_KEY_PREFIX + loginDto.getCodeToken());
        if (ObjUtil.isNull(realCode)) {
            return Result.fail(MessageUtil.getI18nMsg("login.captcha.expire"));
        }
        if (!String.valueOf(realCode).equals(loginDto.getCode())) {
            return Result.fail(MessageUtil.getI18nMsg("login.captcha.fail"));
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            return Result.fail(MessageUtil.getI18nMsg("login.user.password.fail"));
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = jwtService.createToken(loginUser);
        data.put("token", token);
        sysLoginLogService.recordLogin(loginUser.getUsername(), "登录成功");
        return Result.success(data);
    }

}
