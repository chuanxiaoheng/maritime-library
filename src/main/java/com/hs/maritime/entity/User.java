package com.hs.maritime.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* 用户主表
* @TableName users
*/
@TableName("users")
@Data
public class User implements Serializable {

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
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
    * 加密盐
    */
    private String salt;
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
    private LocalDateTime lastLoginTime;
    /**
    * 最后登录ip
    */
    private String lastLoginIp;
    /**
    * 创建时间
    */
    private LocalDateTime createTime;
    /**
    * 更新时间
    */
    private LocalDateTime updateTime;

}
