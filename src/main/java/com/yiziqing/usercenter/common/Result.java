package com.yiziqing.usercenter.common;

/**
 * @program: user-center
 * @create: 2023-10-07 16:29
 * @author: Mr.YiZiqing
 * @description: 结果集
 **/
public class Result {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(20000, data, "OK");
    }

    public static <T> BaseResponse<T> success(T data, String msg) {
        return new BaseResponse<>(20000, data, msg);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> fail(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static <T> BaseResponse<T> fail(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), null, message, description);
    }
}
