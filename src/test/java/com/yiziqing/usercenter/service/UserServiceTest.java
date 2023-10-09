package com.yiziqing.usercenter.service;
import java.util.Date;

import com.yiziqing.usercenter.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: user-center 用户服务测试
 * @create: 2023-09-30 17:27
 * @author: Mr.YiZiqing
 * @description:
 **/
@SpringBootTest
class UserServiceTest {


    @Autowired
    private UserService userService;

    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("yiziqing");
        user.setUserAccount("123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("456");
        user.setEmail("789");

        boolean result = userService.save(user);

        System.out.println(user.getId());

    }

    @Test
    void userRegister() {
        String userAccount = "yiziqing";
        String userPassword = "1123";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
    }


}