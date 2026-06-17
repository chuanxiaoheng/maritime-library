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

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String username;
    private LocalDateTime loginTime;
    private String loginIp;
    private String loginLocation;
    private String deviceInfo;
    private String browser;
    private String os;
    private Integer loginType;
    private Integer status;
}
