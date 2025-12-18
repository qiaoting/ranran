package com.ranran.common.utils;

import com.ranran.common.constant.Constant;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author ranran
 * 请求工具封装
 */
public class RequestUtil {

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.AUTH_HEADER_NAME);
        if (StrUtil.hasText(token) && token.startsWith(Constant.TOKEN_PREFIX)) {
            return token.substring(Constant.TOKEN_PREFIX.length());
        }
        return null;
    }

}
