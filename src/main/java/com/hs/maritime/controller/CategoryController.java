package com.hs.maritime.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.maritime.common.PageResult;
import com.hs.maritime.common.Result;
import com.hs.maritime.dto.CategoryQueryDTO;
import com.hs.maritime.entity.Book;
import com.hs.maritime.entity.Category;
import com.hs.maritime.enums.ResultEnum;
import com.hs.maritime.exceptions.MaritimeException;
import com.hs.maritime.service.BookService;
import com.hs.maritime.service.CategoryService;
import com.hs.maritime.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    @Resource
    private BookService bookService;
    /**
     * 图书分类列表
     * */
    @GetMapping("/list")
    public Result<?> categoryList(){
        // 查询分类列表
        List<Category> categoryList = categoryService.list(Wrappers.<Category>query().orderByAsc("sort_order"));

        // 转换为VO集合
        List<CategoryVO> categoryVOList = categoryList.stream().map(category ->{
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category,categoryVO);
            return categoryVO;
        }) .collect(Collectors.toList());

        // 返回分类列表数据
        return Result.success(categoryVOList);
    }
    /**
     * 图书分类分页条件查询
     * */
    @GetMapping("/page")
    public Result<?> categoryPage(CategoryQueryDTO categoryQueryDTO){

        log.info("页码：{}",categoryQueryDTO.getPageNum());
        log.info("每页展示的数量：{}",categoryQueryDTO.getPageSize());

        // 查询条件
        QueryWrapper<Category> queryWrapper = Wrappers.<Category>query()
                .likeRight(StrUtil.isNotBlank(categoryQueryDTO.getName()),"name",categoryQueryDTO.getName())
                .eq(StrUtil.isNotBlank(categoryQueryDTO.getCode()),"code",categoryQueryDTO.getCode())
                .orderByAsc("sort_order");
        // 根据条件，查询图书分页数据
        IPage<Category> categoryPage =
                categoryService.page(new Page<>(categoryQueryDTO.getPageNum(),categoryQueryDTO.getPageSize()),queryWrapper);


        // 转成VO
        IPage<CategoryVO> categoryVOPage = categoryPage.convert(category -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            return categoryVO;
        });

        // 返回分类列表数据
        return Result.success(PageResult.of(categoryVOPage));
    }

    /**
     * 图书分类新增
     * */
    @PostMapping("/add")
    public Result<?> add(@RequestBody CategoryVO categoryVO){
        Category category = new Category();
        BeanUtils.copyProperties(categoryVO,category);

        // 如果没有填写排序序号，自动获取最大序号
        if(categoryVO.getSortOrder().equals(0)){
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("IFNULL(max(sort_order),0) as sort_order");
            Category categoryMax = categoryService.getOne(queryWrapper);

            // 当前新增分类，序号是最大序号1
            category.setSortOrder(categoryMax.getSortOrder() + 1);
        }
        // 颜色值转换大写
        if(StrUtil.isNotBlank(category.getColor())){
            category.setColor(category.getColor().toUpperCase());
        }
        // 补全字段
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        if(categoryService.save(category)){
            return Result.success();
        }

        return Result.fail();
    }
    /**
     * 图书分类修改
     * */

    @PutMapping("/update")
    public Result<?> updateCategory(@RequestBody CategoryVO categoryVO){
        // 判断id是否存在
        if(categoryVO.getId() == null){
            return Result.fail(ResultEnum.PARAM_VALID);
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryVO,category);
        // 补全字段
        category.setUpdateTime(LocalDateTime.now());

        if(categoryService.updateById(category)){
            return Result.success();
        }

        return Result.fail();
    }
    /**
     * 图书分类删除
     * */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteById(@PathVariable Long id){

        // 判断分类存在关联图书
        boolean exist = bookService.count(Wrappers.<Book>query().eq("category_id",id)) > 0;
        if(exist){
            throw new MaritimeException("该分类存在图书，无法删除");
        }
        if(categoryService.removeById(id)){
            return Result.success();
        }
        return Result.fail();
    }
    /**
     * 图书分类批量删除
     * */
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody  List<Long> ids){

        // 判断分类存在关联图书
        List<Book> bookList = bookService.list(Wrappers.<Book>query().in("category_id",ids));

        // TODO 查询哪些图书分类不能删除
        if(!bookList.isEmpty() ){
            // 将不能删除的id拿出来
            Set<Integer> cannotDeleteIds = bookList.stream()
                    .map(Book::getCategoryId)
                    .collect(Collectors.toSet());
            // 拿到不能删除的集合
            List<Category> cannotDeleteCategoryList = categoryService.listByIds(cannotDeleteIds);
            // 拿到不能删除的名字
            String cannotDeleteCategoryName = cannotDeleteCategoryList.stream().map(Category::getName).collect(Collectors.joining("，"));
            throw new MaritimeException(cannotDeleteCategoryName+ "分类存在图书，无法删除");
        }
        if(categoryService.removeByIds(ids)){
            return Result.success();
        }
        return Result.fail();
    }


}
