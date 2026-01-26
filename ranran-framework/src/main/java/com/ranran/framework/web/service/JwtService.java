package com.ranran.framework.web.service;

import com.ranran.common.constant.Constant;
import com.ranran.common.domain.vo.LoginUser;
import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.RequestUtil;
import com.ranran.common.utils.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ranran
 * Jwt逻辑
 */
@Component
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expire}")
    private int expireTime;
    private final long MILLIS_MINUTE = 60 * 1000L;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String createToken(LoginUser loginUser) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        loginUser.setUuid(uuid);
        refreshToken(loginUser);
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.LOGIN_JWT_KEY, uuid);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();
    }

    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = RequestUtil.getToken(request);
        if (StrUtil.hasText(token)) {
            try {
                Claims claims = parseToken(token);
                String uuid = (String) claims.get(Constant.LOGIN_JWT_KEY);
                if (StrUtil.hasText(uuid)) {
                    String cacheKey = getCacheKey(uuid);
                    LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(cacheKey);
                    if (ObjUtil.isNotNull(loginUser)) {
                        verifyAndRefreshToken(loginUser);
                    }
                    return loginUser;
                }
            } catch (Exception e) {
                log.error("获取登录用户失败：{}", e.getMessage());
            }
        }
        return null;
    }

    public void refreshToken(LoginUser loginUser) {
        if (ObjUtil.isNull(loginUser) || !StrUtil.hasText(loginUser.getUuid())) {
            return;
        }
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        String cacheKey = getCacheKey(loginUser.getUuid());
        redisTemplate.opsForValue().set(cacheKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    public void logout(String uuid) {
        if (StrUtil.hasText(uuid)) {
            String cacheKey = getCacheKey(uuid);
            redisTemplate.delete(cacheKey);
            log.info("用户登出，删除缓存：{}", cacheKey);
        }
    }

    public void verifyAndRefreshToken(LoginUser loginUser) {
        long currentTime = System.currentTimeMillis();
        if (loginUser.getExpireTime() - currentTime <= expireTime * MILLIS_MINUTE) {
            refreshToken(loginUser);
            log.debug("令牌自动刷新，用户名：{}", loginUser.getUsername());
        }
    }

    private Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String getCacheKey(String uuid) {
        return Constant.LOGIN_KEY_PREFIX + uuid;
    }

}