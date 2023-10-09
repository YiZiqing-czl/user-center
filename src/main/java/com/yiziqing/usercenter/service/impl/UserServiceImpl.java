package com.yiziqing.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiziqing.usercenter.common.ErrorCode;
import com.yiziqing.usercenter.exception.YiziqingException;
import com.yiziqing.usercenter.model.User;
import com.yiziqing.usercenter.service.UserService;
import com.yiziqing.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.yiziqing.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.yiziqing.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 86130
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-09-30 17:20:18
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    /**
     * 盐值,将密码进行混淆
     */
    private static final String SALT = "yiziqing";
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"请求参数数据为空");
        }
        if (userAccount.length() < 4) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"账号长度小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"密码小于8位置");
        }
        if (planetCode.length() > 5) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"星球编号大于5位");
        }
        // 账户不包含特殊字符，特殊字符使用正则表达式筛选
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // 使用正则表达式进行校验
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }
        // 密码和校验密码是否相同
        if (!userPassword.equals(checkPassword)) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"两次密码不相同");
        }
        // 账户不能重复，查询数据库当中是否存在相同名称用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"当前用户已存在");
        }
        // 编号不能重复，查询数据库当中是否存在相同编号
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"星球编号重复");
        }
        // 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 将数据插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"数据库插入失败");
        }
        return user.getId();
    }

    /***
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword  用户密码
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"请求参数数据为空");
        }
        if (userAccount.length() < 4) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"账号长度小于4位");
        }
        if (userPassword.length() < 8) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"密码小于8位");
        }
        // 账户不包含特殊字符，特殊字符使用正则表达式筛选
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // 使用正则表达式进行校验
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        }
        //2. 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(wrapper);
        //用户不存在
        if (user == null) {
            log.info("UserServiceImpl.doLogin业务结束,结果:{}", "user用户不存在");
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"当前用户不存在");
        }
        //3.信息脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"参数数据为空");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 注销当前登录用户
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据用户名进行模糊查询
     *
     * @param username
     * @param request
     * @return
     */
    @Override
    public List<User> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"请求参数数据为空");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username", username);
        }
        List<User> users = baseMapper.selectList(wrapper);
        return users.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteUserById(long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new YiziqingException(ErrorCode.PARAMS_ERROR,"请求参数数据错误");
        }
        int i = baseMapper.deleteById(id);
        return i > 0;
    }


    /**
     * 判断是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = null;
        if (userObj instanceof User) {
            user = (User) userObj;
        }
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}




