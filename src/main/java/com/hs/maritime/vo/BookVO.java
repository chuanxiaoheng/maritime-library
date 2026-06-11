package com.hs.maritime.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookVO {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publishDate;
    private Integer categoryId;
    private String location;
    private String description;
    private Double price;
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer borrowedCopies;
    private Integer damagedCopies;
    private Integer status;
    private String cover;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // 冗余分类名称
    private String categoryName;

}
