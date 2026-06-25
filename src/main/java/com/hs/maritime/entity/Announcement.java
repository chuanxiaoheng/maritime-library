package com.hs.maritime.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统公告
 */
@Data
@TableName("Announcements")
public class Announcement {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private Integer publisherId;
    private LocalDateTime publishTime;
    private Integer top;
    private String tag;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
