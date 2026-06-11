package com.hs.maritime.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created On : 2026/6/4.
 * <p>
 * Author : zhukang
 * <p>
 * Description: 图书
 */
@Data
@TableName("books")
public class Book {
    @TableId(type = IdType.AUTO)
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
    private LocalDateTime update_time;
}
