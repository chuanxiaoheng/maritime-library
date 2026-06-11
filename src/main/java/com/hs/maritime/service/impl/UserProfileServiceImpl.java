package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.User;
import com.hs.maritime.entity.UserProfile;
import com.hs.maritime.mapper.UserMapper;
import com.hs.maritime.mapper.UserProfileMapper;
import com.hs.maritime.service.UserProfileService;
import com.hs.maritime.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {
}
