package com.hs.maritime.config;


import com.hs.maritime.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 拦截器拦截请求，自动排除的资源路径集合（说明：配置的路径，是相对应用根路径，即：Controller配置路径,底层会自动忽略yml配置文件里面设置的路径）
    private static final List<String> EXCLUDE_PATH = Arrays.asList(
            "/login","/register","/error","/logout","/favicon.ico",
            "/download/**"
    );

    /**
     * 创建JWT授权拦截器
     * */
    public JWTInterceptor jwtInterceptor(){
        return new JWTInterceptor();
    }

    /**
     * 添加JWT授权拦截器
     * */
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(EXCLUDE_PATH); // 排除拦截请求
    }

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(DATE_PATTERN));
        registrar.registerFormatters(registry);
    }
}
