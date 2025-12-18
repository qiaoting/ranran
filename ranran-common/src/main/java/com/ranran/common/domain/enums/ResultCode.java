package com.ranran.common.domain.enums;

import lombok.Getter;

/**
 * @author ranran
 * Http状态码枚举
 */
@Getter
public enum ResultCode {
    // 通用状态码
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),

    // 业务状态码
    AUTH_ERROR(1001, "认证失败");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
    