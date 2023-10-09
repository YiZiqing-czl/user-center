package com.yiziqing.usercenter.controller;

import com.yiziqing.usercenter.common.BaseResponse;
import com.yiziqing.usercenter.common.ErrorCode;
import com.yiziqing.usercenter.common.Result;
import com.yiziqing.usercenter.exception.YiziqingException;
import com.yiziqing.usercenter.model.User;
import com.yiziqing.usercenter.model.request.UserLoginRequest;
import com.yiziqing.usercenter.model.request.UserRegisterRequest;
import com.yiziqing.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.yiziqing.usercenter.contant.UserConstant.USER_LOGIN_STATE;

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

    @GetMapping("current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) obj;
        if (currentUser == null) {
            throw new YiziqingException(ErrorCode.NOT_LOGIN_ERROR,"请先登录");
        }
        User user = userService.getById(currentUser.getId());
        return Result.success(userService.getSafetyUser(user));
    }


    @PostMapping("register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new YiziqingException(ErrorCode.PARAMS_NULL_ERROR,"前端请求参数数据为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new YiziqingException(ErrorCode.PARAMS_NULL_ERROR,"参数数据为空");
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return Result.success(id);
    }


    @PostMapping("login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new YiziqingException(ErrorCode.PARAMS_NULL_ERROR,"前端请求参数数据为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new YiziqingException(ErrorCode.PARAMS_NULL_ERROR,"参数数据为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return Result.success(user);
    }

    @PostMapping("logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new YiziqingException(ErrorCode.PARAMS_NULL_ERROR,"获取request失败");
        }
        int i = userService.userLogout(request);
        return Result.success(i);
    }

    @GetMapping("search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        return Result.success(userService.searchUsers(username, request));
    }

    @PostMapping("delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"id<=0");
        }
        return Result.success(userService.deleteUserById(id, request));
    }
}
