package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.Role;
import com.hs.maritime.entity.UserLoginLog;
import com.hs.maritime.mapper.RoleMapper;
import com.hs.maritime.mapper.UserLoginLogMapper;
import com.hs.maritime.service.RoleService;
import com.hs.maritime.service.UserLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService {
}
