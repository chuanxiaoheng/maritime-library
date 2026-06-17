package com.hs.maritime.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.maritime.common.PageResult;
import com.hs.maritime.common.Result;
import com.hs.maritime.entity.LibraryCard;
import com.hs.maritime.entity.LibraryCardType;
import com.hs.maritime.entity.User;
import com.hs.maritime.exceptions.MaritimeException;
import com.hs.maritime.service.LibraryCardService;
import com.hs.maritime.service.LibraryCardTypeService;
import com.hs.maritime.service.UserService;
import com.hs.maritime.vo.LibraryCardTypeVO;
import com.hs.maritime.vo.LibraryCardVO;
import com.hs.maritime.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/card")
public class LibraryCardController {
    @Resource
    private LibraryCardService libraryCardService;
    @Resource
    private UserService userService;
    @Autowired
    private LibraryCardTypeService libraryCardTypeService;

    @GetMapping("/list")
    public Result<?> cardList() {

        // 查询读者证列表
        List<LibraryCard> cardList = libraryCardService.list();

        // 转换为VO类型集合
        List<LibraryCardVO> cardVOList = cardList.stream().map(libraryCard -> {
            LibraryCardVO libraryCardVO = new LibraryCardVO();
            BeanUtils.copyProperties(libraryCard, libraryCardVO);
            return libraryCardVO;
        }).collect(Collectors.toList());

        // 返回分类列表数据
        return Result.success(cardVOList);
    }


    @GetMapping("/page")
    public Result<?> cardPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "cardNo", required = false) String cardNo,
                              @RequestParam(value = "userId", required = false) String userId) {

        // 查询条件
        QueryWrapper<LibraryCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(StrUtil.isNotBlank(cardNo), "card_no", cardNo);
        queryWrapper.eq(StrUtil.isNotBlank(userId), "user_id", userId);

        // 根据条件，查询分页数据
        IPage<LibraryCard> cardPage = libraryCardService.page(new Page<>(pageNum, pageSize), queryWrapper);

        // 类型转换
        IPage<LibraryCardVO> cardVOPage = cardPage.convert(libraryCard -> {
            LibraryCardVO libraryCardVO = new LibraryCardVO();
            BeanUtils.copyProperties(libraryCard, libraryCardVO);
            // 计算有效日期
            libraryCardVO.setEffectiveAge(Period.between(libraryCardVO.getIssueDate(), libraryCardVO.getExpireDate()).getYears());
            return libraryCardVO;
        });

        // 返回分页列表
        return Result.success(PageResult.of(cardVOPage));
    }



    /**
     * 新增读者证类型
     */
    @PostMapping("/add")
    public Result<?> addCardType(@RequestBody LibraryCardVO libraryCardVO) {
        // 转换对象
        LibraryCard libraryCard = new LibraryCard();
        BeanUtils.copyProperties(libraryCardVO, libraryCard);

        // 获取选择的读者证类型详情
        LibraryCardType cardType = libraryCardTypeService.getById(libraryCardVO.getTypeId());
        libraryCard.setTypeName(cardType.getName());

        // 校验押金
        if(cardType.getDepositAmount().compareTo(libraryCardVO.getActualDeposit())>0){
            throw new MaritimeException(cardType.getName() + "类型读者证，押金不能低于"+ cardType.getDepositAmount());
        }

        // 计算过期时间
        libraryCard.setExpireDate(LocalDate.now().plusYears(libraryCardVO.getEffectiveAge()));

        // 新增读者证类型
        if (libraryCardService.save(libraryCard)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 更新读者证类型
     */
    @PutMapping("/update")
    public Result<?> updateCard(@RequestBody LibraryCardVO libraryCardVO) {
        // 转换对象
        LibraryCard libraryCard = new LibraryCard();
        BeanUtils.copyProperties(libraryCardVO, libraryCard);

        // 获取选择的读者证类型详情
        LibraryCardType cardType = libraryCardTypeService.getById(libraryCardVO.getTypeId());
        libraryCard.setTypeName(cardType.getName());

        // 计算过期时间
        libraryCard.setExpireDate(LocalDate.now().plusYears(libraryCardVO.getEffectiveAge()));

        // 更新读者证类型
        if (libraryCardService.updateById(libraryCard)) {
            return Result.success();
        }
        return Result.fail();
    }
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteCard(@PathVariable Long id) {

        // 根据id，删除读者证类型
        if (libraryCardService.removeById(id)) {
            return Result.success();
        }
        return Result.fail();
    }

    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatchCard(@RequestBody List<Long> ids) {

        // 根据id集合，批量删除图书
        if (libraryCardService.removeByIds(ids)) {
            return Result.success();
        }
        return Result.fail();
    }


}
