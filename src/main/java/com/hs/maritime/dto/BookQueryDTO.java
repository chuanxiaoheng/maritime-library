package com.hs.maritime.dto;

import lombok.Data;
/**
 * 图书查询条件DTO
 * */
@Data
public class BookQueryDTO {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer category;
    private Integer status;

    // 冗余图书查阅的关键词
    private String keywords;
    private String borrowStatus;

}
