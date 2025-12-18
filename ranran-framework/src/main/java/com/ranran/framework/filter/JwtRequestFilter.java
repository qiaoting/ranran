package com.ranran.framework.filter;

import com.ranran.common.domain.Result;
import com.ranran.common.domain.enums.ResultCode;
import com.ranran.common.domain.vo.LoginUser;
import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.RequestUtil;
import com.ranran.common.utils.ResponseUtil;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.web.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author ranran
 * 请求过滤器
 */
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = RequestUtil.getToken(request);
        if (!StrUtil.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            LoginUser loginUser = jwtService.getLoginUser(request);
            if (ObjUtil.isNull(loginUser)) {
                returnErrorResponse(response);
                return;
            }
            jwtService.verifyAndRefreshToken(loginUser);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginUser, null, loginUser.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            returnErrorResponse(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    public void returnErrorResponse(HttpServletResponse response) throws IOException {
        Result<Void> res = Result.fail(ResultCode.UNAUTHORIZED.getCode(), "登录状态已失效，请重新登录");
        ResponseUtil.writeJson(response, objectMapper.writeValueAsString(res));
    }

}