package com.hs.maritime.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统公告
 */
@Data
public class AnnouncementVO {
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
