package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.LibraryCard;
import com.hs.maritime.mapper.LibraryCardMapper;
import com.hs.maritime.service.LibraryCardService;
import org.springframework.stereotype.Service;

@Service
public class LibraryCardServiceImpl extends ServiceImpl<LibraryCardMapper, LibraryCard> implements LibraryCardService {
}
