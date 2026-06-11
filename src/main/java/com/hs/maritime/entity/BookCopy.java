package com.hs.maritime.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 图书副本
 */
@Data
@TableName("book_copies")
public class BookCopy {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String copyNo;
    private String barcode;
    // 索书号
    private String location;
    private Integer status;
    private Integer borrowCount;
    private String remark;
    private LocalDate purchaseDate;
    private LocalDateTime lastBorrowedTime;
    private LocalDateTime lastReturnedTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
