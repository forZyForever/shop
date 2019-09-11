package com.Nintendo.entity;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Package: com.Nintendo.order.config
 * @Author: ZZM
 * @Date: Created in 2019/9/1 21:23
 * @Address:CN.SZ
 **/
@Component
public class TokenDecode {
    //公钥
    private static final String PUBLIC_KEY = "public.key";

    // 获取令牌
    public String getToken() {
        OAuth2AuthenticationDetails authentication = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        //获取令牌
        String tokenValue = authentication.getTokenValue();
        return tokenValue;
    }

    /**
     * 获取当前的登录的用户的用户信息
     *
     * @return
     */
    public Map<String, String> getUserInfo() {
        //检验jwt,公钥通过rsa进行验证
        Jwt jwt = JwtHelper.decodeAndVerify(getToken(), new RsaVerifier(getPubKey()));
        //获得载荷转化为map
        Map map = JSON.parseObject(jwt.getClaims(), Map.class);
        return map;

    }

    /**
     * 获取非对称加密公钥 Key
     *
     * @return 公钥 Key
     */
    private String getPubKey() {
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return null;
        }
    }
}
