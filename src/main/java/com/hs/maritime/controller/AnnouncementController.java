package com.hs.maritime.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.maritime.common.PageResult;
import com.hs.maritime.common.Result;
import com.hs.maritime.entity.Announcement;
import com.hs.maritime.service.AnnouncementService;
import com.hs.maritime.vo.AnnouncementVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @GetMapping("/last10")
    public Result<?> Page(){
        // 查询最近公告数据
        IPage<Announcement> announcementPage = announcementService.page(new Page<>(1,10),Wrappers.<Announcement>lambdaQuery().orderByDesc(Announcement::getCreateTime));
        // 转成VO
        IPage<AnnouncementVO> announcementVOPage = announcementPage.convert(announcement -> {
            AnnouncementVO announcementVO = new AnnouncementVO();
            BeanUtils.copyProperties(announcement, announcementVO);
            return announcementVO;
        });
        // 返回最近10个公告
        return Result.success(PageResult.of(announcementVOPage));
    }

}
