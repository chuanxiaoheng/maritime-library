package com.hs.maritime.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class LibraryCardVO {
    private Integer  id;
    private String  cardNo;
    private Long userId;
    private String username;
    private Integer typeId;
    private String typeName;
    private BigDecimal actualDeposit;
    private BigDecimal totalFine;
    private Integer currentBorrowed;
    private LocalDate issueDate;
    private LocalDate expireDate;
    private Integer status;
    private Integer delFlag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 有效年限
    private int effectiveAge;
}
