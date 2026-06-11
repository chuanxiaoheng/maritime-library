package com.hs.maritime.common;
/**
 * SystemConstant: 系统常量
 * 说明：企业开放规范中，所有的系统常量，都必须统一定义在公共常量类里，方便维护和扩展
 * */
public class SystemConstant {
    // 用户编号
    public static final String SYSTEM_USER_ID = "userId";
    // 系统用户名
    public static final String SYSTEM_USER_NAME = "username";
    // 授权令牌
    public static final String JWT_AUTH_TOKEN = "Auth-Token";
    // 登录状态
    public static final Integer LOGIN_STATUS_SUCCESS = 1;
    public static final Integer LOGIN_STATUS_FAIL = 0;

    // 登录类型
    public static final Integer LOGIN_TYPE_PWD = 0;
    public static final Integer LOGIN_TYPE_SMS = 1;
    public static final Integer LOGIN_TYPE_OTH = 2;


}
