package com.hs.maritime.dto;

import lombok.Data;

/**
 * 图书查询
 */
@Data
public class BookCopyQueryDTO {
    // 图书管理条件
    // 书名标题
    private String bookTitle;
    // 副本编号
    private String copyNo;
    // 条形码
    private String barcode;
    // 存放位置
    private String location;
    // 状态
    private Integer status;

}
