package com.hs.maritime.interceptor;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.hs.maritime.common.Result;
import com.hs.maritime.common.SystemConstant;
import com.hs.maritime.enums.ResultEnum;
import com.hs.maritime.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录请求来源
        log.info("拦截请求资源：{}",request.getRequestURI());
        // 从请求头或者请求参数中获取Token令牌
        String authToken = request.getHeader(SystemConstant.JWT_AUTH_TOKEN);
        if(StrUtil.isBlank(authToken)){
            authToken = request.getParameter(SystemConstant.JWT_AUTH_TOKEN);
        }
        // 如果Token失效或者未传递,直接拦截该请求
        if(StrUtil.isBlank(authToken) || !JWTUtils.validateToken(authToken)){
            log.warn("请求资源,鉴权失败,Token令牌为空或失效");
            // 响应结果
            this.responseResult(response, ResultEnum.UNAUTHORIZED.getCode(),ResultEnum.UNAUTHORIZED.getMsg());
            return false;
        }

        Claims claims = JWTUtils.parseToken(authToken);
        // 解析失败，拦截该请求
        if(claims == null) {
            log.warn("请求资源,鉴权失败,Token令牌无效");
            return false;
        }
        return true;
    }

    //  请求鉴权失败，响应结果
    private void responseResult(HttpServletResponse response,Integer code,String msg){
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().write(JSON.toJSONString(Result.fail(code,msg)));
        } catch (IOException e) {
           e.printStackTrace();
        }

    }
}
