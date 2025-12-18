package com.ranran.common.exception;

import com.ranran.common.domain.enums.ResultCode;
import lombok.Getter;

/**
 * @author ranran
 * 自定义业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    private final String message;

    public BusinessException(String message) {
        this(ResultCode.ERROR.getCode(), message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
