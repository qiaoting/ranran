package com.ranran.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ranran
 * spring上下文工具封装
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        if (applicationContext == null) {
            throw new RuntimeException("Spring上下文未初始化");
        }
        try {
            return (T) applicationContext.getBean(beanName);
        } catch (BeansException e) {
            throw new RuntimeException("获取Bean失败：beanName=" + beanName, e);
        }
    }

    public static <T> T getBeanByClassName(String className) {
        if (applicationContext == null) {
            throw new RuntimeException("Spring上下文未初始化");
        }
        try {
            Class<?> clazz = Class.forName(className);
            return (T) applicationContext.getBean(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("类不存在：className=" + className, e);
        } catch (BeansException e) {
            throw new RuntimeException("获取Bean失败：className=" + className, e);
        }
    }

}
