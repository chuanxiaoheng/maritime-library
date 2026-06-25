package com.hs.maritime.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BorrowRecordVO {
    private Long id;
    private String recordNo;
    private Integer cardId;
    private Long userId ;
    private String userName;
    private Long bookId;
    private String bookName;
    private String bookIsbn;
    private BigDecimal bookPrice;
    private Long bookCopyId;
    private LocalDate borrowTime;
    private LocalDate  dueTime;
    private LocalDate returnTime;
    private Integer overdueDays;
    private BigDecimal overdueFine;
    private Integer status;
    private Long operatorId;
    private String remark;
    private Integer renewalCount;
    private Integer maxRenewals;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // 借阅天数
    private int borrowDays;
}
