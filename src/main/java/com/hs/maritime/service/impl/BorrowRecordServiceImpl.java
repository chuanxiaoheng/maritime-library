package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.BorrowRecord;
import com.hs.maritime.mapper.BorrowRecordMapper;
import com.hs.maritime.service.BorrowRecordService;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 *
 * */
@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {


    @Override
    public Map<String, Object> getUserBorrowStats(Long userId) {
        return this.baseMapper.selectUserBorrowStats(userId);
    }
}
