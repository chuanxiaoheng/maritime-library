package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.BookCopy;
import com.hs.maritime.mapper.BookCopyMapper;
import com.hs.maritime.service.BookCopyService;
import org.springframework.stereotype.Service;

/**
 * 图书副本业务实现类
 */
@Service
public class BookCopyServiceImpl extends ServiceImpl<BookCopyMapper, BookCopy> implements BookCopyService {
}
