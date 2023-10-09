package com.yiziqing.usercenter.exception;

import com.yiziqing.usercenter.common.BaseResponse;
import com.yiziqing.usercenter.common.ErrorCode;
import com.yiziqing.usercenter.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: user-center
 * @create: 2023-10-07 17:32
 * @author: Mr.YiZiqing
 * @description: 全局异常捕获处理
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(YiziqingException.class)
    public BaseResponse yiziqingExceptionHandler(YiziqingException e) {
        log.info("GlobalExceptionHandler.yiziqingExceptionHandler业务结束,结果:{}", e);
        return Result.fail(e.getCode(),e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.info("GlobalExceptionHandler.runtimeExceptionHandler业务结束,结果:{}", e);
        return Result.fail(ErrorCode.SYSTEM_ERROR, "");
    }
}
