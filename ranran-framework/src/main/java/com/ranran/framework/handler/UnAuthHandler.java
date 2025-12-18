package com.ranran.framework.handler;

import com.ranran.common.domain.Result;
import com.ranran.common.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author ranran
 * 未认证处理类
 */
@Component
public class UnAuthHandler implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ResponseUtil.writeJson(response, objectMapper.writeValueAsString(
                Result.fail("认证失败，无法访问")));
    }
}
