package com.yiziqing.usercenter.service;

import com.yiziqing.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 86130
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-09-30 17:20:18
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /***
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 根据用户名进行模糊查询
     *
     * @param username
     * @param request
     * @return
     */
    List<User> searchUsers(String username, HttpServletRequest request);

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    boolean deleteUserById(long id, HttpServletRequest request);

    /**
     * 注销当前登录用户
     */
    int userLogout(HttpServletRequest request);
}
