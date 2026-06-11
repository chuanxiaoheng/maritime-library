package com.hs.maritime.utils;

import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * JWT（JSON Web Token）工具类在实际开发中通常包含以下一些常用方法：
 * 1. 生成JWT：用于生成JWT令牌并返回令牌字符串。
 * 2. 解析JWT：用于解析JWT令牌，验证签名，并获取其中的声明信息。
 * 3. 验证JWT：用于验证JWT的有效性，包括验证签名、过期时间等。
 * 4. 刷新JWT：用户还在操作，马上要快过期时，延长其有效期(无感刷新)。
 * 5. 其他辅助方法：例如获取JWT中的特定声明信息，验证JWT是否包含某个声明，等等
 * 其中生成和解析token是必须的，其他方法根据项目需求设计来决定写不写。
 */

public class JWTUtils {
    // token 有效期
    public static final Long JWT_TTL = 3600000L;    // 60 * 60 * 1000  一个小时
    public static final String JWT_SECRET = "jwt123";// Jwt令牌信息

    // 创建token方法
    public static String createToken(String subject, Map<String,Object> claims, Long expireTime) {     // id, 信息, 过期时间

        // 如果令牌有效期为空或者小于0，则默认设置有效期1小时
        if (expireTime == null || expireTime <= 0) {
            expireTime = JWT_TTL;
        }

        // Jwt令牌信息
        return Jwts.builder()
                .setId(Claims.ID)//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为令牌的唯一标识。
                .setSubject(subject) // 设置主题,一般为用户名，也可以是json数据
                .setIssuedAt(new Date())// 设置签发时间
                .addClaims(claims) // 设置负载
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // 设置签名算法以及密匙
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))// 设置令牌过期时间
                .compact();// 生成令牌
    }

    /**
     * 解析令牌数据
     * @param token
     * @return
     * @throws Exception
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     *  验证token令牌
     * @param token 令牌
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 刷新Token
     * @param token 旧的Token令牌
     * @return 新的Token令牌
     */
    public static String refreshToken(String token) {
        try {
            // 解析旧的Token，获取负载信息
            Claims claims = parseToken(token);
            // 生成新的Token，设置过期时间和签名算法等参数
            return createToken(claims.getSubject(), claims, JWT_TTL * 2);
        } catch (Exception e) {
            throw new RuntimeException("无法刷新令牌！", e);
        }
    }


    /**
     * 从令牌中获取主题信息
     * @param token 令牌
     * @return 主题信息(用户类型)
     */
    public static String getUsernameFromToken(String token) {
        try {
            Claims claims = parseToken(token); // 解析令牌，获取负载信息
            return claims.getSubject(); // 返回主题信息
        } catch (Exception e) {
            throw new RuntimeException("无法从令牌中获取主题。", e);
        }
    }

    /**
     * 从令牌中获取用户ID信息
     * @param token 令牌
     * @return 主题信息(用户类型)
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? ((Number) claims.get("userId")).longValue() : null;
    }
}