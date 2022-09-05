package com.example.auth.handler;

import org.springframework.context.ApplicationListener;

import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

import com.example.core.utils.StringUtils;

/**
 * @Author lixianglong
 * @create 2022/8/9 下午12:46
 */
public abstract class AbstractAuthSuccessHandler implements ApplicationListener<AuthenticationSuccessEvent> {
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event)
    {
        Authentication authentication = (Authentication) event.getSource();
        if (StringUtils.isNotEmpty(authentication.getAuthorities()))
        {
            handle(authentication);
        }
    }

    /**
     * 处理登录成功方法
     */
    public abstract void handle(Authentication authentication);
}
