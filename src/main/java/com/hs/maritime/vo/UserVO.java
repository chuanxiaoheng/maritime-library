package com.hs.maritime.vo;

import java.time.LocalDateTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserVO {

    /**
    * 主键ID
    */
    private Long id;
    /**
    * 用户名
    */
    private String username;
    /**
    * 昵称
    */
    private String nickname;
    /**
    * 密码
    */
    private String password;
    /**
    * 手机号
    */
    private String phone;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 头像URL
    */
    private String avatar;
    /**
    * 性别：0-女 1-男 2-未知
    */
    private Integer sex;
    /**
    * 生日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;
    /**
    * 真实姓名
    */
    private String realName;
    /**
    * 联系地址
    */
    private String address;
    /**
    * 个人简介
    */
    private String intro;
    /**
    * 角色编号——关联角色表
    */
    private Integer roleId;
    /**
    * 部门编号——关联部门表
    */
    private Integer deptId;
    /**
    * 会员 0-否 1-是
    */
    private Integer vip;
    /**
    * 状态 0-禁用 1-正常 2-锁定 3-注销
    */
    private Integer status;
    /**
    * 最后登录时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime lastLoginTime;
    /**
    * 最后登录ip
    */
    private String lastLoginIp;
    /**
    * 创建时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
    * 更新时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
