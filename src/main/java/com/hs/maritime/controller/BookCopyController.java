package com.hs.maritime.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.hs.maritime.common.PageResult;
import com.hs.maritime.common.Result;
import com.hs.maritime.dto.BookCopyQueryDTO;
import com.hs.maritime.entity.Book;
import com.hs.maritime.entity.BookCopy;
import com.hs.maritime.service.BookCopyService;
import com.hs.maritime.service.BookService;
import com.hs.maritime.vo.BookCopyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 图书副本操作入口
 */
@RestController
@RequestMapping("/copy")
public class BookCopyController {

    @Resource
    private BookCopyService bookCopyService;

    @Resource
    private BookService bookService;

    /**
     * 根据条件，查询图书副本列表
     */
    @GetMapping("/selectPage")
    public Result<?> selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                BookCopyQueryDTO bookCopyQuery) {

        // 根据条件，查询图书副本分页列表
        QueryWrapper<BookCopy> queryWrapper = Wrappers.<BookCopy>query()
                .likeRight(StrUtil.isNotBlank(bookCopyQuery.getBookTitle()), "book_title", bookCopyQuery.getBookTitle())
                .eq(StrUtil.isNotBlank(bookCopyQuery.getCopyNo()), "copy_no", bookCopyQuery.getCopyNo())
                .eq(StrUtil.isNotBlank(bookCopyQuery.getBarcode()),  "barcode", bookCopyQuery.getBarcode())
                .eq(bookCopyQuery.getStatus() != null,  "status", bookCopyQuery.getStatus())
                .orderByAsc("id");

        // 根据条件，分页查询图书副本列表
        IPage<BookCopy> bookCopyPage = bookCopyService.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 转换类型
        IPage<BookCopyVO> bookCopyVOPage = bookCopyPage.convert(bookCopy -> {
            BookCopyVO bookCopyVO = new BookCopyVO();
            BeanUtils.copyProperties(bookCopy, bookCopyVO);
            return bookCopyVO;
        });

        // 根据条件查询图书副本列表，并返回
        return Result.success(PageResult.of(bookCopyVOPage));
    }

    /**
     * 更新图书副本
     */
    @PutMapping("/update")
    public Result update(@RequestBody BookCopyVO bookCopyVO){

        // 转换为BookCope对象
        BookCopy bookCopy = new BookCopy();
        BeanUtils.copyProperties(bookCopyVO, bookCopy);

        // 更新图书副本
        if (bookCopyService.updateById(bookCopy)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 新增图书副本
     */
    @PostMapping("/add")
    @Transactional
    public Result add(@RequestBody BookCopyVO bookCopyVO){

        // 根据图书编号，获取图书详情
        Book book = bookService.getById(bookCopyVO.getBookId());

        // 输入图书不存在
        if (null == book) {
            return Result.fail("该图书编号不存在！");
        }

        // 转换为BookCope对象
        BookCopy bookCopy = new BookCopy();
        BeanUtils.copyProperties(bookCopyVO, bookCopy);

        // 添加图书信息
        bookCopy.setBookTitle(book.getTitle());

        // 新增图书副本
        if (bookCopyService.save(bookCopy)) {

            // 更新图书部副本总数量，可借数量
            UpdateWrapper<Book> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("total_copies", book.getTotalCopies() + 1);
            updateWrapper.set("available_copies", book.getAvailableCopies() + 1);
            updateWrapper.eq("id", book.getId());
            bookService.update(updateWrapper);
            return Result.success();
        }
        return Result.fail();
    }

    /**
     *  删除图书副本
     */
    @DeleteMapping("/delete/{id}")
    @Transactional
    public Result delete(@PathVariable Long id){

        // 根据副本编号，获取副本详情
        BookCopy bookCopy = bookCopyService.getById(id);

        // 删除图书副本
        if (bookCopyService.removeById(id)) {

            // 获取图书详情
            Book book = bookService.getById(bookCopy.getBookId());

            // 更新图书部副本总数量，可借数量
            UpdateWrapper<Book> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("total_copies", book.getTotalCopies() - 1);
            updateWrapper.set("available_copies", book.getAvailableCopies() - 1);
            updateWrapper.eq("id", book.getId());
            bookService.update(updateWrapper);

            return Result.success();
        }
        return Result.fail();
    }

}
