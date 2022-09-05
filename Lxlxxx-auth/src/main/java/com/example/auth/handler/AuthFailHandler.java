package com.example.auth.handler;



import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.core.utils.ServletUtils;

/**
 * @Author lixianglong
 * @create 2022/8/9 下午12:34
 */
@Component
@Slf4j
public class AuthFailHandler extends AbstractAuthFailHandler{



    @Override
    protected void handle(AuthenticationException authenticationExceçtion, Authentication authentication) {
        HttpServletRequest request = ServletUtils.getRequest();

        String url = request.getRequestURI();

        String username = (String) authentication.getPrincipal();

        log.info("用户：{} 授权失败，url：{}", username, url);
    }

}
