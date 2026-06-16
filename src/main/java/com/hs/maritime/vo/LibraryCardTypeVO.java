package com.hs.maritime.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class LibraryCardTypeVO {
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
