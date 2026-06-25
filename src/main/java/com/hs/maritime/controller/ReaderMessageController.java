package com.hs.maritime.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.maritime.common.PageResult;
import com.hs.maritime.common.Result;
import com.hs.maritime.entity.ReaderMessage;
import com.hs.maritime.service.ReaderMessageService;
import com.hs.maritime.vo.ReaderMessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/message")
public class ReaderMessageController {

    @Resource
    private ReaderMessageService readerMessageService;

    @GetMapping("/last10")
    public Result<?> lastMessage(){
        // 查询最近留言数据
        IPage<ReaderMessage> readerMessagePage = readerMessageService.page(new Page<>(1,10),Wrappers.<ReaderMessage>lambdaQuery().orderByDesc(ReaderMessage::getCreateTime));
        // 转成VO
        IPage<ReaderMessageVO> readerMessageVOPage = readerMessagePage.convert(readerMessage -> {
            ReaderMessageVO readerMessageVO = new ReaderMessageVO();
            BeanUtils.copyProperties(readerMessage, readerMessageVO);
            return readerMessageVO;
        });
        // 返回最近10个留言
        return Result.success(PageResult.of(readerMessageVOPage));
    }

}
