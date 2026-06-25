package com.hs.maritime.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hs.maritime.common.Result;
import com.hs.maritime.entity.Book;
import com.hs.maritime.entity.BorrowRecord;
import com.hs.maritime.entity.User;
import com.hs.maritime.service.BookService;
import com.hs.maritime.service.BorrowRecordService;
import com.hs.maritime.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/dashboard")
@RestController
public class DashboardController {

    @Resource
    private BookService bookService;
    @Resource
    private UserService userService;
    @Resource
    private BorrowRecordService borrowRecordService;

    @GetMapping("/statsData")
    public Result<?> statsData(){
        // 定义统计数据集合
        Map<String,Long> statsMap = new HashMap<>();

        // 统计图书数量
        Long bookCount = bookService.count(Wrappers.<Book>lambdaQuery().eq(Book::getStatus,1));
        statsMap.put("bookCount",bookCount);

        // 统计读者数量
        Long readerCount = userService.count(Wrappers.<User>lambdaQuery()
                .eq(User::getStatus,1)
                .eq(User::getRoleId,3)
        );
        statsMap.put("readerCount",readerCount);
        // 当前借阅数量
        // 当天起始时间:yyyy-MM-dd 00:00:00
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Long borrowCount = borrowRecordService.count(Wrappers.<BorrowRecord>lambdaQuery()
                .ge(BorrowRecord::getCreateTime,startOfDay));
        statsMap.put("borrowCount",borrowCount);

        // 当天归还数量
        Long returnCount = borrowRecordService.count(Wrappers.<BorrowRecord>lambdaQuery()
                .ge(BorrowRecord::getReturnTime,startOfDay));
        statsMap.put("returnCount",returnCount);


        return Result.success(statsMap);
    }

    @GetMapping("/hotRank")
    public Result<?> hotBookRank(){
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        // 设置查询字段
        queryWrapper.select("book_id","book_name as bookName","count(*) as borrowCount ");
        // 设置分组
        queryWrapper.groupBy("book_id, book_name");
        // 设置排序：按照数量排序，取前10条
        queryWrapper.orderByDesc("borrowCount").last("limit 10");

        System.out.println(queryWrapper.getSqlSelect());
        // 执行查询
        List<Map<String,Object>> hotBookRankList = borrowRecordService.listMaps(queryWrapper);
        // 返回结果
        return Result.success(hotBookRankList);
    }

    @GetMapping("/bookProportion")
    public Result<?> bookCategoryProportion(){
        // 集合数据
        Map<String,Object> proportionMap = new HashMap<>();
        // 图书总数
        proportionMap.put("bookTotal",bookService.count(Wrappers.<Book>lambdaQuery().eq(Book::getStatus,1)));

        // 分组统计
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("category_id","count(*) as categoryCount")
                .eq("status",1)
                .groupBy("category_id");

        // 获取数据
        List<Map<String,Object>> categoryProportionList = bookService.listMaps(queryWrapper);
        proportionMap.put("categoryProportionList",categoryProportionList);

        return Result.success(proportionMap);

    }
}
