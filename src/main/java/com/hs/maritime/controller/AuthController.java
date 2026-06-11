package com.hs.maritime.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hs.maritime.common.Result;
import com.hs.maritime.common.SystemConstant;
import com.hs.maritime.entity.Role;
import com.hs.maritime.entity.User;
import com.hs.maritime.entity.UserLoginLog;
import com.hs.maritime.entity.UserProfile;
import com.hs.maritime.enums.ResultEnum;
import com.hs.maritime.enums.UserStatusEnum;
import com.hs.maritime.exceptions.MaritimeException;
import com.hs.maritime.service.RoleService;
import com.hs.maritime.service.UserLoginLogService;
import com.hs.maritime.service.UserProfileService;
import com.hs.maritime.service.UserService;
import com.hs.maritime.utils.DeviceLogUtils;
import com.hs.maritime.utils.JWTUtils;
import com.hs.maritime.utils.MD5Utils;
import com.hs.maritime.vo.UserVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户授权入口
 * */
@Slf4j
@RestController
public class AuthController {

    @Resource
    private UserService userService;
    @Resource
    private UserProfileService userProfileService;
    @Resource
    private UserLoginLogService userLoginLogService;
    @Resource
    private RoleService roleService;

    /**
     * 测试前端的token令牌
     * */
    @GetMapping("/testAuthToken")
    public Result<String> testAuthToken(HttpServletRequest request) {
        // 获取token
        String token = request.getParameter("Auth-Token");
        if(StrUtil.isEmpty(token)){
            token = request.getHeader("Auth-Token");
        }
        log.info("jwt token:{}",token);
        if(JWTUtils.validateToken(token)){
            log.info("jwt token 令牌有效");
        }else{
            log.info("jwt token 令牌无效");
            return Result.fail(ResultEnum.UNAUTHORIZED);
        }
        return Result.success();
    }

    /**
     * 测试JWT令牌
     * */
    @PostMapping("/testJwt")
    public Result<String> testJwt(){
        // 添加负载
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SystemConstant.SYSTEM_USER_ID,101);

        // 生成Token
        String token = JWTUtils.createToken("admin",claimsMap,null);
        log.info("jwt token:{}",token);

        // 验证token
        log.info("jwt check:{}",JWTUtils.validateToken(token));

        // 解析token
        Claims claims = JWTUtils.parseToken(token);

        log.info("jwt claims:{}",claims);

        return Result.success(token);
    }
    /**
     * 用户注册
     * */
    @PostMapping("/register")
    public Result<?> register(@RequestBody UserVO userVO){
        // 根据用户名获取用户
        User dbUser = userService.getOne(new QueryWrapper<User>().eq("username",userVO.getUsername()));
        // 判断用户名是否已经被占用
        if(dbUser != null){
            throw new MaritimeException("该用户名已被占用！");
        }
        // 创建用户对象
        User user = new User();
        BeanUtils.copyProperties(userVO,user);

        // 设置加密盐：12位字符串
        user.setSalt(RandomUtil.randomString(16));
        // 设置加密密码
        user.setPassword(MD5Utils.MD5Encode(userVO.getPassword()+ user.getSalt(),"UTF-8"));
        // 设置默认昵称
        user.setNickname("N" + RandomUtil.randomString(6));
        // 执行注册用户
        if(userService.save(user)){
            // 注册成功
            return Result.success("注册成功",null);
        }
        // 注册失败
        return Result.fail();
    }
    /**
     * 用户登录
     * */
    @PostMapping("/login")
    public Result<?> login(@RequestBody UserVO userVO, HttpServletRequest request){

        // 根据用户名获取用户
        User dbUser = userService.getOne(new QueryWrapper<User>().eq("username",userVO.getUsername()));
        // 判断用户是否存在
        if(ObjectUtils.isEmpty(dbUser)){
            throw new MaritimeException("用户不存在");
        }
        // 判断用户状态是否正常
        if(!dbUser.getStatus().equals(UserStatusEnum.NORMAL.getCode())){
            throw new MaritimeException(UserStatusEnum.getMsgByCode(dbUser.getStatus()));
        }
        // 判断用户登录角色是否匹配
        if(!dbUser.getRoleId().equals(userVO.getRoleId())){
            throw new MaritimeException("该用户角色不匹配");
        }
        // 获取登录设备信息
        DeviceLogUtils.DeviceInfo deviceInfo = DeviceLogUtils.parseDeviceInfo(request);
        // 封装用户登录记录信息
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setUserId(dbUser.getId());
        userLoginLog.setUsername(dbUser.getUsername());
        userLoginLog.setLoginTime(LocalDateTime.now());
        userLoginLog.setLoginIp(DeviceLogUtils.getIpAddr(request));
        userLoginLog.setDeviceInfo(StrUtil.join("-",deviceInfo.getDeviceType(),deviceInfo.getOs()));
        userLoginLog.setOs(deviceInfo.getOs());
        userLoginLog.setBrowser(deviceInfo.getBrowser());
        userLoginLog.setLoginLocation(deviceInfo.getIp());
        userLoginLog.setLoginType(SystemConstant.LOGIN_TYPE_PWD);
        userLoginLog.setStatus(SystemConstant.LOGIN_STATUS_SUCCESS);

        // 校验密码是否正确
        if(!StrUtil.equals(dbUser.getPassword(),MD5Utils.MD5Encode(userVO.getPassword()+dbUser.getSalt(),"UTF-8"))){
            userLoginLog.setStatus(SystemConstant.LOGIN_STATUS_FAIL);
            // TODO 记录登录日志
           return Result.fail(500,"用户名或者密码错误");
        }
        // 用户名和密码正确，登录成功
        // 更新登录时间
        dbUser.setLastLoginIp(userLoginLog.getLoginIp());
        dbUser.setLastLoginTime(userLoginLog.getLoginTime());
        userService.updateById(dbUser);

        // 记录登录成功日志
        userLoginLogService.save(userLoginLog);

        // 拷贝数据
        BeanUtils.copyProperties(dbUser,userVO);

        // 添加自定义数据（负载）
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SystemConstant.SYSTEM_USER_ID,dbUser.getId());

        // 设置授权token
        userVO.setAuthToken(JWTUtils.createToken(dbUser.getUsername(),claimsMap,null));
        // 去除密码
        userVO.setPassword(null);

        // 查询用户角色
        Role userRole = roleService.getById(dbUser.getRoleId());
        // 设置用户角色名称
        userVO.setRoleName(userRole.getRoleName());

        // 查询用户资料
        UserProfile userProfile = userProfileService.getOne(new QueryWrapper<UserProfile>().eq("user_id", dbUser.getId()));
        BeanUtils.copyProperties(userProfile,userVO);

        return Result.success(userVO);
    }
}














