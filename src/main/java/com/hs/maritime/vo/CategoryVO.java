package com.hs.maritime.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryVO {
    private Integer id;
    private String name;
    private String color;
    private String description;
    private Integer sortOrder;
    private Integer parentId;
    private String code;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
