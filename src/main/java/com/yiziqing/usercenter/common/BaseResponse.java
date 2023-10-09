package com.yiziqing.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: user-center
 * @create: 2023-10-07 16:21
 * @author: Mr.YiZiqing
 * @description: 统一返回结果集
 **/

@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String msg;

    private String description;

    public static final Long serialVersionUID = 1L;

    public BaseResponse(int code, T data, String msg, String description) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = description;
    }

    public BaseResponse(int code, T data, String msg) {
        this(code, data, msg, "");
    }


    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
