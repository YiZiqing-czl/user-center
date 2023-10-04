package com.yiziqing.usercenter.controller;

import com.yiziqing.usercenter.model.User;
import com.yiziqing.usercenter.model.request.UserLoginRequest;
import com.yiziqing.usercenter.model.request.UserRegisterRequest;
import com.yiziqing.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: user-center
 * @create: 2023-10-03 16:23
 * @author: Mr.YiZiqing
 * @description: 接口管理
 **/
@RestController //该接口适用于编写restful风格的api，返回会之都是默认json
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }


    @PostMapping("login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        return userService.searchUsers(username, request);
    }

    @PostMapping("delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (id <= 0) {
            return false;
        }
        return userService.deleteUserById(id, request);
    }
}
