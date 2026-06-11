package com.hs.maritime.exceptions;

import com.hs.maritime.common.Result;
import com.hs.maritime.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MaritimeExceptionHandler异常统一处理，实现异常统一返回（统一返回结果给前端）
 * */
@Slf4j
@ControllerAdvice // 控制器增强注解，可以将所有controller控制器组件增加统一处理
public class MaritimeExceptionHandler {


    // 自定义MaritimeException 异常处理方法，任何Controller控制器抛出该异常，都会被当前方法处理
    @ExceptionHandler(MaritimeException.class) // 异常处理注解，指定需要处理的异常类型
    @ResponseBody
    public Result<?> handleMaritimeException(MaritimeException me){
        // 生产环境，必须打印日志，不允许打印异常堆栈
        log.error("业务异常：{}",me.getMessage());

        // 统一返回异常结果
        return Result.fail(me.getErrCode(),me.getMessage());
    }
    // 如果有其他异常类型需要处理，直接类似于上面的定义，定义多个即可

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleException(Exception exception){
        log.error("系统未知异常：{}",exception.getMessage());
        return Result.fail(ResultEnum.FAIL);
    }
}
