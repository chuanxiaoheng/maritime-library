package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hs.maritime.entity.LibraryCardType;
import com.hs.maritime.mapper.LibraryCardTypeMapper;
import com.hs.maritime.service.LibraryCardTypeService;
import org.springframework.stereotype.Service;

/**
 * 读者证类型业务实现类
 */
@Service
public class LibraryCardTypeServiceImpl extends ServiceImpl<LibraryCardTypeMapper, LibraryCardType> implements LibraryCardTypeService {
}
