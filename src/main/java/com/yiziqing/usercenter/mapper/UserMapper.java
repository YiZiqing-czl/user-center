package com.yiziqing.usercenter.mapper;

import com.yiziqing.usercenter.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86130
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-09-30 17:20:18
* @Entity com.yiziqing.usercenter.model.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




