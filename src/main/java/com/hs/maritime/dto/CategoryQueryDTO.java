package com.hs.maritime.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图书分类查询条件DTO
 * */
@Data
public class CategoryQueryDTO{
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;
    private String code;
}
