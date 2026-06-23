package com.hs.maritime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.maritime.entity.BorrowRecord;

import java.util.Map;

public interface BorrowRecordService extends IService<BorrowRecord>  {
    /***
     * 自定义接口：查询用户借阅数据
     */
    Map<String,Object> getUserBorrowStats(Long userId);
}
