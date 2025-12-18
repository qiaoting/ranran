package com.ranran.common.utils;

import com.ranran.common.constant.Constant;
import com.ranran.common.domain.vo.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ranran
 * spring security工具封装
 */
public class SecurityUtil {

    public static String bcryptEncode(String content) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(content);
    }

    public static boolean passwordMatch(String password, String encodePassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, encodePassword);
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static LoginUser getLoginUser() {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    public static boolean isAdmin(Long userId) {
        return ObjUtil.isNotNull(userId) && Constant.ADMIN_ID.equals(userId);
    }

}
