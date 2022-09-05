package com.example.security.feign;

import feign.RequestInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lixianglong
 * @create 2022/8/22 下午4:53
 */
@Configuration
public class Oauth2FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new OAuth2FeignRequestInterceptor();
    }
}
