package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.Book;
import com.hs.maritime.mapper.BookMapper;
import com.hs.maritime.service.BookService;
import org.springframework.stereotype.Service;

/**
 *  图书业务实现类
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
}
