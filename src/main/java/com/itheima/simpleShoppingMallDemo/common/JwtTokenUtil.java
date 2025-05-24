package com.itheima.simpleShoppingMallDemo.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JwtTokenUtil：负责生成和解析 JWT Token 的工具类
 */
@Component
public class JwtTokenUtil {

    /** Base64-URL 编码后的秘钥，解码后至少 512 bits */
    private static final String SECRET_BASE64 =
            "miYwB6VO-b7ZO-JmE0Q7r5mkHlGtZH8HgNOCs-D_4DtYJ7Ii7o_UpwYgS95Bdxu-VubiO1Flgeb9i7BnbUBFMw==";

    /** 解码后的 SecretKey，用于签名与验证 */
    private final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRET_BASE64));

    /** Token 过期时间（秒），这里设置为 1 小时 */
    private static final long EXPIRATION = 3_600L;

    /**
     * 生成 JWT Token
     *
     * @param username 用户名（也可以放入用户 ID、角色等）
     * @return 带签名、带过期时间的完整 JWT
     */
    public String generateToken(String username,Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)                         // 放入主体
                .claim("userId", userId)                  // 自定义字段
                .setIssuedAt(now)                             // 签发时间
                .setExpiration(new Date(now.getTime() + EXPIRATION * 1_000)) // 过期时间
                .signWith(key, SignatureAlgorithm.HS512)      // 使用 HS512 算法与 key 签名
                .compact();
    }

    /**
     * 从 Token 中提取用户名
     *
     * @param token 客户端传入的 JWT 字符串
     * @return payload 中的 subject（用户名）
     * @throws JwtException 如果解析或验证失败则抛出异常
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)           // 验签时用相同 key
                .build()
                .parseClaimsJws(token)        // 解析并验证签名与过期
                .getBody()
                .getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    /**
     * 校验 Token 是否有效
     *
     * @param token 客户端传入的 JWT 字符串
     * @return true=合法且未过期；false=签名不匹配或已过期
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 包括 ExpiredJwtException, MalformedJwtException, SignatureException 等
            // 根据需求可以分别捕获记录日志
            System.err.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }
}
