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
@TableName("reader_messages")
public class ReaderMessage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String username;
    private String content;
    private String reply;
    private LocalDateTime replyTime;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
