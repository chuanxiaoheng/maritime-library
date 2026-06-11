package com.hs.maritime.entity;


import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("categories")
public class Category {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private String color;
    private String description;
    private Integer sortOrder;
    private Integer parentId;
    private String code;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
