package com.hs.maritime.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.maritime.common.PageResult;
import com.hs.maritime.common.Result;
import com.hs.maritime.dto.BookQueryDTO;
import com.hs.maritime.entity.Book;
import com.hs.maritime.entity.Category;
import com.hs.maritime.service.BookService;
import com.hs.maritime.service.CategoryService;
import com.hs.maritime.service.FileService;
import com.hs.maritime.vo.BookVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
    @Resource
    private BookService bookService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private FileService fileService;
    @Value("${file.cover-dir}")
    private String coverDir;

    /**
     * 根据条件，获取图书列表
     * */
    @GetMapping("/bookPage")
    public Result<?> bookPage(@RequestParam(defaultValue = "1")Integer pageNum,
                              @RequestParam(defaultValue = "10")Integer pageSize,
                              BookQueryDTO bookQueryDTO){


        log.info("页码：{}",pageNum);
        log.info("每页展示的数量：{}",pageSize);
        // 查询条件
        QueryWrapper<Book> queryWrapper = Wrappers.<Book>query()
                .likeRight(StrUtil.isNotBlank(bookQueryDTO.getTitle()),"title",bookQueryDTO.getTitle())
                .eq(StrUtil.isNotBlank(bookQueryDTO.getAuthor()),"author",bookQueryDTO.getAuthor())
                .eq(StrUtil.isNotBlank(bookQueryDTO.getIsbn()),"isbn",bookQueryDTO.getIsbn())
                .eq(StrUtil.isNotBlank(bookQueryDTO.getPublisher()),"publisher",bookQueryDTO.getPublisher())
                .eq(bookQueryDTO.getCategoryId() != null,"category_id",bookQueryDTO.getCategoryId())
                .eq(bookQueryDTO.getStatus() != null,"status",bookQueryDTO.getStatus())
                .orderByDesc("create_time");
        // 根据条件，查询图书分页数据
        IPage<Book> bookPage =
                bookService.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 自定义sql语句，图书表关联数据
        // 查询分类数据，程序内处理
        Map<Integer,String> categoryMap = categoryService.list()
                .stream().collect(Collectors.toMap(Category::getId,Category::getName));
        // 转成VO
        IPage<BookVO> bookVOPage = bookPage.convert(book -> {
            BookVO bookVO = new BookVO();
            BeanUtils.copyProperties(book, bookVO);
            bookVO.setCategoryName(categoryMap.get(bookVO.getCategoryId()));
            return bookVO;
        });
        return Result.success(PageResult.of(bookVOPage));
    }

    /**
     * 新增图书
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody BookVO bookVO){
        // 转换对象
        Book book = new Book();
        BeanUtils.copyProperties(bookVO,book);
        // 新增图书
        if(bookService.save(book)){
            return Result.success();
        }

        return Result.fail();
    }
    /**
     * 修改图书
     */
    @PutMapping("/update")
    public Result<?> updateBook(@RequestBody BookVO bookVO){
        // 转换对象
        Book book = new Book();
        BeanUtils.copyProperties(bookVO,book);
        // 修改图书
        if(bookService.updateById(book)){
            return Result.success();
        }

        return Result.fail();
    }
    /**
     * 删除图书
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteById(@PathVariable Long id){
        // 根据id,删除图书
       if(bookService.removeById(id)){
           return Result.success();
       }
        return Result.fail();
    }
    /**
     * 批量删除图书
     */
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteByIds(@RequestBody List<Long> ids){
        if(bookService.removeByIds(ids)){
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 上传图书封面文件
     */
    @PostMapping("/uploadCover")
    public Result<?> uploadCover(@RequestParam("file")MultipartFile file) throws IOException {
        // 上传文件
        String coverName = fileService.upload(file,coverDir);
        // 封装可访问的图书封面地址
        String coverUrl = "http://localhost:8080/api/download/cover/" + coverName;

        return Result.success(coverUrl);
    }



}
