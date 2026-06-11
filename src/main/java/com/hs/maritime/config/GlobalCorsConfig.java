package com.hs.maritime.config;

import com.hs.maritime.common.SystemConstant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
/**
 * 全局跨域配置，后端支持
 * */

@Configuration
public class GlobalCorsConfig {

    // 添加跨域过滤器
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");// 1.开放哪些ip，端口，域名的访问权限，*代表开放所有
        config.addAllowedHeader("*");// 2.允许HTTP请求中携带哪些头信息，*代表允许所有
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));// 3.开放哪些请求方式
        config.setAllowCredentials(true);// 4.是否允许发送cookie,允许

        // 5.默认只会暴露简单的响应头，如果有特殊响应头信息，必须指定（指定暴露的响应头参数，可以被前端访问）
        config.setExposedHeaders(Arrays.asList(SystemConstant.JWT_AUTH_TOKEN));

        // 6.添加映射路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 所有映射路由都实行全局跨域访问权限配置
        source.registerCorsConfiguration("/**",config);


        // 7.返回跨域过滤器对象
        return new CorsFilter(source);

    }
}
