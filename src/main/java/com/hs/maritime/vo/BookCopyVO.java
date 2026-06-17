package com.hs.maritime.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 图书副本
 */
@Data
public class BookCopyVO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String copyNo;
    private String barcode;
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
