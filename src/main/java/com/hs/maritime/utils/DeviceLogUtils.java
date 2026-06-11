package com.hs.maritime.utils;

import cn.hutool.core.util.StrUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.HttpServletRequest;

/**
 * 登录设备信息记录工具类
 */
@Slf4j
public class DeviceLogUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "127.0.0.1";

    /**
     * 获取客户端真实IP地址
     * 处理反向代理情况
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 处理多级代理，取第一个IP
        if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        // 处理本地IPv6地址
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = LOCALHOST_IP;
        }
        
        return ip;
    }

    /**
     * 解析User-Agent获取设备详细信息
     */
    public static DeviceInfo parseDeviceInfo(HttpServletRequest request) {
        String userAgentStr = request.getHeader("User-Agent");
        DeviceInfo info = new DeviceInfo();
        info.setIp(getIpAddr(request));

        if (StrUtil.isBlank(userAgentStr)) {
            info.setBrowser(UNKNOWN);
            info.setOs(UNKNOWN);
            info.setDeviceType(UNKNOWN);
            return info;
        }

        try {
            // 使用 eu.bitwalker 库解析
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
            
            // 获取浏览器信息
            Browser browser = userAgent.getBrowser();
            info.setBrowser(browser.getName());
            info.setBrowserVersion(userAgent.getBrowserVersion().toString());
            
            // 获取操作系统信息
            OperatingSystem os = userAgent.getOperatingSystem();
            info.setOs(os.getName());
            
            // 获取设备类型 (桌面/移动/平板等)
            info.setDeviceType(os.getDeviceType().getName());
            
        } catch (Exception e) {
            log.error("解析User-Agent失败: {}", userAgentStr, e);
            info.setBrowser(UNKNOWN);
            info.setOs(UNKNOWN);
            info.setDeviceType(UNKNOWN);
        }
        
        return info;
    }

    /**
     * 设备信息实体
     */
    @Data
    public static class DeviceInfo {
        private String ip;
        private String browser;
        private String browserVersion;
        private String os;
        private String deviceType;
    }
}
