package com.hs.maritime.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 失败
    FAIL(500,"操作失败"),
    // 参数异常
    PARAM_VALID(400,"参数非法"),
    // 未授权
    UNAUTHORIZED(401,"请求未授权"),
    // 无权限
    ACCESS_FORBIDDEN(403,"访问权限不足"),
    // 资源不存在
    NOT_FOUND(404,"请求资源不存在"),
    // 方式不支持
    METHOD_NOT_SUPPORT(405,"请求方式不支持"),
    // 业务异常
    BUSINESS_ERROR(501,"系统业务异常"),
    // 网络异常
    NETWORK_ERROR(601,"系统网络异常"),
    // 数据库异常
    DATABASE_ERROR(701,"数据库访问异常");

    // 状态码
    private Integer code;
    // 状态说明
    private String msg;

    ResultEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
    // 通用方法：可以根据状态码，获取对应说明
    public static String getMsgByCode(Integer code){
        // 遍历枚举实例
        for(ResultEnum resultEnum: ResultEnum.values()){
            // 判断状态码是否存在，存在返回对应的说明
            if(resultEnum.getCode().equals(code)){
                return resultEnum.getMsg();
            }
        }
        return null;
    }
}
