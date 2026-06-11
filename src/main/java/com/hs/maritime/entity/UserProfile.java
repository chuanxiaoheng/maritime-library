package com.hs.maritime.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_profiles")
public class UserProfile {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 学历
     */
    private String education;
    /**
     * 职业
     */
    private String occupation;
    /**
     * 兴趣爱好
     */
    private String hobby;
    /**
     * 个人签名
     */
    private String personalSign;
    /**
     * 信用分
     */
    private Integer creditScore;
    /**
     * 是否接受邮件提醒 1-接受 0-拒绝
     */
    private Integer receiveEmail;
    /**
     * 是否接受到期提醒 1-接受 0-拒绝
     */
    private Integer receiveDue;
    /**
     * 是否接受短信 1-接受 0-拒绝
     */
    private Integer receiveSms;
    /**
     * 是否接受公告 1-接受 0-拒绝
     */
    private Integer receiveNotice;
    /**
     * 个人信息是否可见 1-可见 0-隐藏
     */
    private Integer profileVisible;
    /**
     * 借阅历史是否可见 1-可见 0-隐藏
     */
    private Integer borrowHisVisible;
}
