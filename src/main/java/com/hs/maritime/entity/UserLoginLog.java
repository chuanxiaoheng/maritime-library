package com.hs.maritime.entity;


import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_login_logs")
public class UserLoginLog {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 登录ip
     */
    private String loginIp;
    /**
     * 登录地理位置
     */
    private String loginLocation;
    /**
     * 登录设备信息
     */
    private String deviceInfo;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 登录方式：0-密码 1-短信 2-第三方
     */
    private Integer loginType;
    /**
     * 状态：1-成功 0-失败
     */
    private Integer status;
}
