package com.hs.maritime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.maritime.entity.ReaderMessage;
import com.hs.maritime.mapper.ReaderMessageMapper;
import com.hs.maritime.service.ReaderMessageService;
import org.springframework.stereotype.Service;

@Service
public class ReaderMessageServiceImpl extends ServiceImpl<ReaderMessageMapper, ReaderMessage> implements ReaderMessageService {
}
