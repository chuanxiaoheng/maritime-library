package com.hs.maritime.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@TableName("library_cards")
public class LibraryCard {

    @TableId(type = IdType.AUTO)
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
    private LocalDate  expireDate;
    private Integer status;
    private Integer delFlag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
