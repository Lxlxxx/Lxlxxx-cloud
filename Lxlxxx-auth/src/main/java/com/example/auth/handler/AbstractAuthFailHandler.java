package com.example.auth.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @Author lixianglong
 * @create 2022/8/9 下午12:22
 */
public abstract class AbstractAuthFailHandler implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        AuthenticationException authenticationExceçtion = event.getException();
        Authentication authentication = (Authentication) event.getSource();

        handle(authenticationExceçtion, authentication);
    }

    protected abstract void handle(AuthenticationException authenticationExceçtion, Authentication authentication);


}
