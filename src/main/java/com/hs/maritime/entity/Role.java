package com.hs.maritime.entity;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("roles")
public class Role {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 排序字段
     */
    private Integer roleSort;
    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;
    /**
     * 角色备注
     */
    private String remark;
    /**
     * 逻辑删除： 0-正常 1-已经删除
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
