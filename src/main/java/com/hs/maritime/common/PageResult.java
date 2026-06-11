package com.hs.maritime.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页统一结果类，符合前后端分离最佳实践
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 8575799808933029326L;
    // 页码
    private long pageNum;
    // 分页条数
    private long pageSize;
    // 总条数
    private long total;
    // 总页数
    private long pages;
    // 分页数据
    private List<T> list;

    // 通用构建分页结果对象方法,基于内存分页
    public static <T> PageResult<T> of(long pageNum,long pageSize,long total,long pages,List<T> list){
        return new PageResult<>(pageNum,pageSize,total,pages,list);
    }
    // 通用构建分页结果对象方法，基于MP
    public static <T> PageResult<T> of(IPage<T> page){
        return new PageResult<>(page.getCurrent(),page.getSize(),page.getTotal(),page.getPages(),page.getRecords());
    }


}
