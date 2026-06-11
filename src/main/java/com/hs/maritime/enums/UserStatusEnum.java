package com.hs.maritime.enums;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    // 正常
    NORMAL(1,"正常"),
    // 禁用
    FORBIDDEN(0,"用户已禁用"),
    // 已锁定
    LOCKED(2,"用户已锁定"),
    // 已注销
    OFFED(3,"用户不存在");


    // 状态码
    private Integer code;
    // 状态说明
    private String msg;

    UserStatusEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
    // 通用方法：可以根据状态码，获取对应说明
    public static String getMsgByCode(Integer code){
        // 遍历枚举实例
        for(UserStatusEnum resultEnum: UserStatusEnum.values()){
            // 判断状态码是否存在，存在返回对应的说明
            if(resultEnum.getCode().equals(code)){
                return resultEnum.getMsg();
            }
        }
        return null;
    }
}
