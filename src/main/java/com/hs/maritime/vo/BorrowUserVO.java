package com.hs.maritime.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 借阅用户VO类
 */
@Data
public class BorrowUserVO {
    private Long id;
    private String userName;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer sex;
    private String realName;
    private String address;
    private String intro;
    private Long roleId;
    private String roleName;
    private Integer status;

    // 读者证号
    private String cardNo;
    // 读者证类型
    private String cardType;
    // 读者证状态
    private Integer cardStatus;

    // 最大借阅数量
    private int maxBorrowBooks;
    // 最大借阅天数
    private int maxBorrowDays;

    // 借阅数量
    private int borrowingCount;
    // 归还数量
    private int returnedCount;
    // 逾期数量
    private int overdueCount;
    // 赔偿数量
    private int compensateCount;
    // 逾期费用
    private BigDecimal overdueFee;
    // 最近借阅图书记录
    private List<BorrowRecordVO> borrowRecords;

}
