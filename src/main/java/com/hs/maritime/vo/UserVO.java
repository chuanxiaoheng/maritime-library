package com.hs.maritime.vo;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private Integer sex;
    private Date birthday;
    private String realName;
    private String address;
    private String intro;
    private Integer roleId;
    private Integer deptId;
    private Integer vip;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String authToken;
    private String roleName;
    private Long userId;
    private String education;
    private String occupation;
    private String hobby;
    private String personalSign;
    private Integer creditScore;
    private Integer receiveEmail;
    private Integer receiveDue;
    private Integer receiveSms;
    private Integer receiveNotice;
    private Integer profileVisible;
    private Integer borrowHisVisible;
}
