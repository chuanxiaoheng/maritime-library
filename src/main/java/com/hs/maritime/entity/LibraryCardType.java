package com.hs.maritime.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


@Data
@TableName("library_card_types")
public class LibraryCardType {

    @TableId(type = IdType.AUTO)
    private Integer  id;
    private String  name;
    private Integer  maxBooks;
    private Integer  maxDays;
    private Integer  renewCount;
    private Integer  renewDays;
    private BigDecimal  overdueFee;
    private BigDecimal depositAmount;
    private Integer  status;
    private String  description;
}
