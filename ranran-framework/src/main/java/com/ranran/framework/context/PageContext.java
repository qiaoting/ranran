package com.ranran.framework.context;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author ranran
 * 分页上下文
 */
public class PageContext {

    private static final ThreadLocal<Page<?>> PAGE_HOLDER = new ThreadLocal<>();

    public static void setPage(Page<?> page) {
        PAGE_HOLDER.set(page);
    }

    public static <T> Page<T> getPage() {
        Page<T> page = (Page<T>) PAGE_HOLDER.get();
        return page;
    }

    public static void clear() {
        PAGE_HOLDER.remove();
    }
}
