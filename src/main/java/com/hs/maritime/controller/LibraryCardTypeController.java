package com.hs.maritime.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.maritime.common.PageResult;
import com.hs.maritime.common.Result;
import com.hs.maritime.entity.LibraryCard;
import com.hs.maritime.entity.LibraryCardType;
import com.hs.maritime.exceptions.MaritimeException;
import com.hs.maritime.mapper.LibraryCardTypeMapper;
import com.hs.maritime.service.LibraryCardService;
import com.hs.maritime.service.LibraryCardTypeService;
import com.hs.maritime.vo.LibraryCardTypeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 读者证类型入口
 */
@RestController
@RequestMapping("/cardType")
public class LibraryCardTypeController {

    @Resource
    private LibraryCardTypeService libraryCardTypeService;
    @Resource
    private LibraryCardService libraryCardService;

    /**
     * 读者证类型列表
     * */
    @GetMapping("/list")
    public Result<?> cardTypeList() {

        // 查询读者证类型列表
        List<LibraryCardType> cardTypeList = libraryCardTypeService.list();

        // 转换为VO类型集合
        List<LibraryCardTypeVO> cardTypeVOList = cardTypeList.stream().map(libraryCardType -> {
            LibraryCardTypeVO libraryCardTypeVO = new LibraryCardTypeVO();
            BeanUtils.copyProperties(libraryCardType, libraryCardTypeVO);
            return libraryCardTypeVO;
        }).collect(Collectors.toList());

        // 返回分类列表数据
        return Result.success(cardTypeVOList);
    }

    /**
     * 根据条件，获取读者证类型分页数据
     */
    @GetMapping("/page")
    public Result<?> cardTypePage(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "name", required = false) String name) {

        // 查询条件
        QueryWrapper<LibraryCardType> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(StrUtil.isNotBlank(name), "name", name);

        // 根据条件，查询分页数据
        IPage<LibraryCardType> cardTypePage = libraryCardTypeService.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 类型转换
        IPage<LibraryCardTypeVO> cardTypeVOPage = cardTypePage.convert(libraryCardType -> {
            LibraryCardTypeVO libraryCardTypeVO = new LibraryCardTypeVO();
            BeanUtils.copyProperties(libraryCardType, libraryCardTypeVO);
            return libraryCardTypeVO;
        });

        // 返回分页列表
        return Result.success(PageResult.of(cardTypeVOPage));
    }

    /**
     * 新增读者证类型
     */
    @PostMapping("/add")
    public Result<?> addCardType(@RequestBody LibraryCardTypeVO libraryCardTypeVO) {
        // 转换对象
        LibraryCardType libraryCardType = new LibraryCardType();
        BeanUtils.copyProperties(libraryCardTypeVO, libraryCardType);

        // 新增读者证类型
        if (libraryCardTypeService.save(libraryCardType)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 更新读者证类型
     */
    @PutMapping("/update")
    public Result<?> updateCardType(@RequestBody LibraryCardTypeVO libraryCardTypeVO) {
        // 转换对象
        LibraryCardType libraryCardType = new LibraryCardType();
        BeanUtils.copyProperties(libraryCardTypeVO, libraryCardType);

        // 更新读者证类型
        if (libraryCardTypeService.updateById(libraryCardType)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     *  删除读者证类型
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteCardType(@PathVariable Long id) {

        // TODO 判断该类型是否存在读者证关系，存在不可以删除
        List<LibraryCard> libraryCardList = libraryCardService.list(Wrappers.<LibraryCard>query().in("type_id", id));
        if(!libraryCardList.isEmpty()){
            throw new MaritimeException("该分类关联了读者证，请先删除该分类下面的读者证！");
        }

        // 根据id，删除读者证类型
        if (libraryCardTypeService.removeById(id)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 批量删除读者证类型
     */
    @Transactional
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatchCardType(@RequestBody List<Long> ids) {

        // TODO 判断该类型是否存在关联读者证
        List<LibraryCard> libraryCardList = libraryCardService.list(Wrappers.<LibraryCard>query().in("type_id", ids));
        if(!libraryCardList.isEmpty()){
            throw new MaritimeException("选中的部分分类关联了读者证，请先删除该分类下面的读者证！");
        }

        // 根据id集合，批量删除图书
        if (libraryCardTypeService.removeByIds(ids)) {
            return Result.success();
        }
        return Result.fail();
    }
}
