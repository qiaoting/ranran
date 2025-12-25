package com.ranran.common.utils;

import com.ranran.common.constant.Constant;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ranran
 * 响应工具封装
 */
@Slf4j
public class ResponseUtil {

    public static void writeJson(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding(Constant.UTF8);
            response.getWriter().print(string);
        } catch (IOException e) {
            log.error("响应json数据失败 {}", e.getMessage());
        }
    }

}
