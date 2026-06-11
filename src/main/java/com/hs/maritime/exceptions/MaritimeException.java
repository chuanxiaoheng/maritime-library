package com.hs.maritime.exceptions;

import com.hs.maritime.enums.ResultEnum;
import lombok.Getter;

/**
 * 自定义异常
 * 说明：实际的业务开发中，通常所有的业务相关异常，都使用自定义异常类型对象抛出，不允许使用系统异常
 * */
@Getter
public class MaritimeException extends RuntimeException{
    // 异常码
    private Integer errCode;

    public MaritimeException() {
    }
    // 支持自定义异常信息
    public MaritimeException(String errMsg) {
        super(errMsg);
        this.errCode = ResultEnum.BUSINESS_ERROR.getCode();
    }
    // 支持自定义异常码和异常信息
    public MaritimeException(Integer errCode,String errMsg){
        super(errMsg);
        this.errCode = errCode;
    }
    // 支持自定义异常枚举
    public MaritimeException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.errCode = resultEnum.getCode();
    }



}
