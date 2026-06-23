package com.hs.maritime.dto;

import lombok.Data;

/**
 * 图书分类查询条件DTO
 * */
@Data
public class RecordQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long userId;
    private String bookName;
    private Integer status;
}
