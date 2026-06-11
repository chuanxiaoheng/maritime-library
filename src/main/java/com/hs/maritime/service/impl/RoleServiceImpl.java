package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.Role;
import com.hs.maritime.entity.User;
import com.hs.maritime.mapper.RoleMapper;
import com.hs.maritime.mapper.UserMapper;
import com.hs.maritime.service.RoleService;
import com.hs.maritime.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
