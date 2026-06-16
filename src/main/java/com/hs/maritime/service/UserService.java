package com.hs.maritime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.maritime.entity.User;

import java.util.List;

/**
 * 用户业务接口
 * */
public interface UserService extends IService<User> {

    List<User> getWithoutCardUsers();
}
