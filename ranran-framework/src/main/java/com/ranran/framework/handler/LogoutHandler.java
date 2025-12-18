package com.ranran.framework.handler;

import com.ranran.common.domain.Result;
import com.ranran.common.domain.enums.ResultCode;
import com.ranran.common.domain.vo.LoginUser;
import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.ResponseUtil;
import com.ranran.framework.web.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author ranran
 * 退出处理类
 */
@Component
public class LogoutHandler implements LogoutSuccessHandler {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = jwtService.getLoginUser(request);
        if (ObjUtil.isNotNull(loginUser)) {
            jwtService.logout(loginUser.getUuid());
        }
        Result res = Result.success("退出成功");
        ResponseUtil.writeJson(response, objectMapper.writeValueAsString(res));
    }
}
