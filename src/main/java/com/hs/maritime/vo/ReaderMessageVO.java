package com.hs.maritime.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统公告
 */
@Data
public class ReaderMessageVO {
    private Long id;
    private Long userId;
    private String username;
    private String content;
    private String reply;
    private LocalDateTime replyTime;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
