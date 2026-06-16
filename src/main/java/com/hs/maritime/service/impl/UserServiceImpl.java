package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.User;
import com.hs.maritime.mapper.UserMapper;
import com.hs.maritime.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> getWithoutCardUsers() {
        // 调用Mapper接口自定义方法，查询无读者证用户
        return this.baseMapper.selectWithCardUserList();
    }
}
