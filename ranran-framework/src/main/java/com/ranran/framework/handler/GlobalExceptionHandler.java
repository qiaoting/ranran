package com.ranran.framework.handler;

import com.ranran.common.domain.Result;
import com.ranran.common.domain.enums.ResultCode;
import com.ranran.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ranran
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("请求地址:'{}', 错误信息:{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR.getCode(), "系统繁忙，请稍后再试");
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        log.error("请求地址:'{}', 错误信息:{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR.getCode(), "系统故障，请稍后再试");
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址:'{}', 错误信息:{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR.getCode(), "运行繁忙，请稍后再试");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',错误信息'{}'", requestURI, e.getMessage());
        return Result.fail(ResultCode.FORBIDDEN.getCode(), "没有权限，请联系管理员");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                      HttpServletRequest request) {
        log.error("请求地址:'{}', 错误信息:{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR.getCode(), "操作不支持，方法不支持");
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public Result handleMissingPathVariableException(HttpServletRequest request, MissingPathVariableException e) {
        log.error("请求地址:'{}', 错误信息:{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR.getCode(), "操作不支持，缺少变量");
    }

    @ExceptionHandler(BindException.class)
    public Result handleBindException(HttpServletRequest request, BindException e) {
        log.error("请求地址:'{}', 错误信息:{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR.getCode(), "操作不支持，校验失败");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("请求地址:'{}', 错误信息:{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.ERROR.getCode(), "操作不支持，参数不合法");
    }

}