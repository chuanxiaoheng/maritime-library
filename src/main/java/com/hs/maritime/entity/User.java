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
*/
@TableName("users")
@Data
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
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
    private String salt;
    private Integer vip;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
