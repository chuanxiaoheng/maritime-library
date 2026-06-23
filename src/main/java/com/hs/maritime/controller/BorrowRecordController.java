package com.hs.maritime.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.maritime.common.PageResult;
import com.hs.maritime.common.Result;
import com.hs.maritime.dto.RecordQueryDTO;
import com.hs.maritime.entity.*;
import com.hs.maritime.enums.ResultEnum;
import com.hs.maritime.exceptions.MaritimeException;
import com.hs.maritime.service.*;
import com.hs.maritime.utils.JWTUtils;
import com.hs.maritime.vo.BorrowRecordVO;

import com.hs.maritime.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * 借阅记录操作入口
 * */
@RestController
@RequestMapping("/borrow")
public class BorrowRecordController {

    @Resource
    private BookService bookService;

    @Resource
    private LibraryCardService libraryCardService;

    @Resource
    private LibraryCardTypeService libraryCardTypeService;

    @Resource
    private BookCopyService bookCopyService;

    @Resource
    private BorrowRecordService borrowRecordService;
    /**
     * 新增借阅记录
     */
    @PostMapping("/add")
    @Transactional
    public Result<?> add(@RequestBody List<BorrowRecordVO> borrowRecordVOList,
                         @RequestHeader("Auth-Token")String authToken){
        // 遍历
        borrowRecordVOList.stream().forEach(borrowRecordVO -> {
            // 转换对象
            BorrowRecord borrowRecord = new BorrowRecord();
            BeanUtils.copyProperties(borrowRecordVO,borrowRecord);

            // 根据借阅图书编号来获取图书详情
            Book book = bookService.getById(borrowRecord.getBookId());

            // 封装借阅记录
            borrowRecord.setRecordNo("JY"+ DateUtil.format(DateUtil.date(),"yyyyMMddHHmmss")+
                    borrowRecord.getBookId()+
                    borrowRecord.getUserId()
            );
            borrowRecord.setOperatorId(JWTUtils.getUserIdFromToken(authToken));
            borrowRecord.setBookName(book.getTitle());
            borrowRecord.setBookPrice(book.getPrice());
            borrowRecord.setBookIsbn(book.getIsbn());
            // 根据用户，查询读者证
            LibraryCard libraryCard = libraryCardService.getOne(Wrappers.<LibraryCard>query().eq("user_id",borrowRecord.getUserId()));
            borrowRecord.setCardId(libraryCard.getId());

            // 借阅时间
            borrowRecord.setBorrowTime(LocalDate.now());
            borrowRecord.setDueTime(LocalDate.now().plusDays(borrowRecordVO.getBorrowDays()));

            // 根据读者证，获取读者证类型
            LibraryCardType libraryCardType = libraryCardTypeService.getById(libraryCard.getTypeId());
            borrowRecord.setRenewalCount(0);
            borrowRecord.setMaxRenewals(libraryCardType.getRenewCount());

            // 自动分配副本（系统自动从可借副本中，选取一个），只查找状态为1（可借阅)
            BookCopy bookCopy = bookCopyService.list(Wrappers.<BookCopy>query().orderByDesc("copy_no")
                    .eq("book_id",borrowRecord.getBookId())
                    .eq("status",1)
            ).get(0);
            borrowRecord.setBookCopyId(bookCopy.getId());
            // 新增借阅记录
            if(!borrowRecordService.save(borrowRecord)){
                throw new MaritimeException("添加借阅记录失败！");
            }
            // 更新副本状态,2代表已借出
            bookCopyService.update(Wrappers.<BookCopy>update()
                    .set("status",2)
                    .set("borrow_count",bookCopy.getBorrowCount() + 1)
                    .eq("id",bookCopy.getId())
            );
            // 更新图书数据
            bookService.update(Wrappers.<Book>update()
                    .set("available_copies",book.getAvailableCopies() - 1)
                    .set("borrowed_copies",book.getBorrowedCopies() + 1)
                    .eq("id",book.getId())
            );
        });

        return Result.success();
    }
    /**
     * 根据条件，查询借阅记录分页数据
     * */
    @GetMapping("/page")
    public Result<?> recordPage(RecordQueryDTO recordQueryDTO){

        // 查询条件
        LambdaQueryWrapper<BorrowRecord> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(recordQueryDTO.getUserId() != null, BorrowRecord::getUserId,recordQueryDTO.getUserId())
                .likeRight(StrUtil.isNotBlank(recordQueryDTO.getBookName()),BorrowRecord::getBookName,recordQueryDTO.getBookName())
                .eq(recordQueryDTO.getStatus() != null, BorrowRecord::getStatus,recordQueryDTO.getStatus());

        // 根据条件，查询图书分页数据
        IPage<BorrowRecord> borrowRecordPage = borrowRecordService.page(new Page<>(recordQueryDTO.getPageNum(),recordQueryDTO.getPageSize()),queryWrapper);

        // 转成VO
        IPage<BorrowRecordVO> borrowRecordVOPage = borrowRecordPage.convert( borrowRecord-> {
            BorrowRecordVO borrowRecordVO = new BorrowRecordVO();
            BeanUtils.copyProperties(borrowRecord, borrowRecordVO);
            return borrowRecordVO;
        });

        // 返回分类列表数据
        return Result.success(PageResult.of(borrowRecordVOPage));
    }


    @PutMapping("/update")
    public Result<?> updateCategory(@RequestBody BorrowRecordVO borrowRecordVO){
        // 判断id是否存在
        if(borrowRecordVO.getId() == null){
            return Result.fail(ResultEnum.PARAM_VALID);
        }

        LambdaUpdateWrapper<BorrowRecord> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(BorrowRecord::getDueTime,borrowRecordVO.getDueTime())
                .set(BorrowRecord::getStatus,borrowRecordVO.getStatus())
                .set(BorrowRecord::getRemark,borrowRecordVO.getRemark())
                .eq(BorrowRecord::getId,borrowRecordVO.getId());

        // 更新借阅记录
        if(borrowRecordService.update(updateWrapper)){
            return Result.success();
        }

        return Result.fail();
    }

}
