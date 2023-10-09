package com.yiziqing.usercenter.common;

import lombok.Getter;

/**
 * @program: user-center
 * @create: 2023-10-07 16:45
 * @author: Mr.YiZiqing
 * @description: 错误码
 **/
@Getter
public enum ErrorCode {

    SYSTEM_ERROR(50000,"系统内部异常",""),

    PARAMS_ERROR(40000, "请求参数异常", ""),

    PARAMS_NULL_ERROR(40001, "请求数据为空", ""),

    NOT_LOGIN_ERROR(40100, "未登录", ""),

    NO_AUTH_ERROR(40101, "无访问权限", "");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
