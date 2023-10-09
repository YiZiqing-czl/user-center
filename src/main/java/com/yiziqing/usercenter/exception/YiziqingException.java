package com.yiziqing.usercenter.exception;

import com.yiziqing.usercenter.common.ErrorCode;
import lombok.Getter;

/**
 * @program: user-center
 * @create: 2023-10-07 17:11
 * @author: Mr.YiZiqing
 * @description: 全局异常处理
 **/
@Getter
public class YiziqingException extends RuntimeException {

    private final int code;

    private final String description;


    public YiziqingException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public YiziqingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public YiziqingException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }
}
