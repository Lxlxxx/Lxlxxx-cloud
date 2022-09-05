package com.example.auth.handler;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.core.utils.ServletUtils;
import com.example.security.login.LoginUser;

/**
 * @Author lixianglong
 * @create 2022/8/9 下午12:22
 */
@Component
@Slf4j
public class AuthSuccessHandler extends  AbstractAuthSuccessHandler{

    @Override
    public void handle(Authentication authentication) {
        HttpServletRequest request = ServletUtils.getRequest();

        String url = request.getRequestURI();

        if (authentication.getPrincipal() instanceof LoginUser) {
            LoginUser user = (LoginUser) authentication.getPrincipal();

            String username = user.getUsername();

            log.info("用户：{} 授权成功，url：{}", username, url);
        }
    }
}
