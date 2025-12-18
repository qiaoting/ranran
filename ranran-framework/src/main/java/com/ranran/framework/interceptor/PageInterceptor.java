package com.ranran.framework.interceptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.common.constant.PageConstant;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.context.PageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author ranran
 * 分页拦截器
 */
@Component
public class PageInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int pageNum = 1;
        int pageSize = 10;
        try {
            if (StrUtil.hasText(request.getParameter(PageConstant.PAGE_NUM))) {
                pageNum = Integer.parseInt(request.getParameter(PageConstant.PAGE_NUM));
            }
            if (StrUtil.hasText(request.getParameter(PageConstant.PAGE_SIZE))) {
                pageSize = Integer.parseInt(request.getParameter(PageConstant.PAGE_SIZE));
            }
        } catch (NumberFormatException e) {
        }
        pageNum = Math.max(pageNum, 1);
        pageSize = Math.max(Math.min(pageSize, 100), 1);
        Page<?> page = new Page<>(pageNum, pageSize);
        PageContext.setPage(page);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PageContext.clear();
    }
}
