package com.yiziqing.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: user-center
 * @create: 2023-10-03 16:31
 * @author: Mr.YiZiqing
 * @description: 前端注册请求封装参数
 **/
@Data
public class UserRegisterRequest implements Serializable {
    public static final Long serialVersionUID = 1L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}
