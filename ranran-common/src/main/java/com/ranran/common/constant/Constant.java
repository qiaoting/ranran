package com.ranran.common.constant;

/**
 * @author ranran
 * 系统常量
 */
public class Constant {

    public static final String UTF8 = "UTF-8";

    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String LOGIN_JWT_KEY = "login_jwt";

    /** redis key相关 **/
    public static final String LOGIN_KEY_PREFIX = "login:";
    public static final String KAPTCHA_KEY_PREFIX = "kaptcha:";
    public static final String CONFIG_KEY_PREFIX = "config:";

    public static final Long ADMIN_ID = 1L;
    public static final String ADMIN_CODE = "admin";
    public static final String ADMIN_PERMISSION = "*:*:*";

}
