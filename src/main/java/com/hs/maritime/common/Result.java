package com.hs.maritime.common;

import com.hs.maritime.enums.ResultEnum;
import lombok.Data;

/**
 * Result：统一结果类
 * */
@Data
public class Result<T> {
    /**
     * 状态码
     * */
    private Integer code;
    /**
     * 状态说明
     * */
    private String msg;
    /**
     * 结果数据
     * */
    private T data;

    public static <T> Result<T> success(){
        return success(ResultEnum.SUCCESS.getMsg(),null);
    }
    public static <T>  Result<T>  success(T data){
        return success(ResultEnum.SUCCESS.getMsg(),data);
    }
    public static <T>  Result<T>  success(String msg,T data){
        Result<T> result = new Result<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


    /**
     * 返回失败，通用
     * */
    public static <T>  Result<T>  fail(){
        return fail(ResultEnum.FAIL);
    }

    /**
     * 返回失败提示信息
     * */
    public static <T> Result<T> fail(String msg){
        Result<T> result = new Result<>();
        result.setMsg(msg);
        return result;
    }
    /**
     * 返回失败，带自定义说明
     * */
    public static <T> Result<T> fail(Integer code,String msg){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    /**
     * 返回失败，指定结果枚举
     * */
    public static <T> Result<T> fail(ResultEnum resultEnum){
        Result<T> result = new Result<>();
        result.setMsg(resultEnum.getMsg());
        result.setCode(resultEnum.getCode());
        return result;
    }


}
