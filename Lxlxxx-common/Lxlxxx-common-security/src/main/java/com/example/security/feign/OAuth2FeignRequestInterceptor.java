package com.example.security.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import com.example.core.constant.SecurityConstants;

/**
 * @Author lixianglong
 * @create 2022/8/22 下午4:56
 */
@Component
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails)
        {
            OAuth2AuthenticationDetails dateils = (OAuth2AuthenticationDetails) authentication.getDetails();
            //设置请求token信息
            template.header(HttpHeaders.AUTHORIZATION,
                    String.format("%s %s", SecurityConstants.BEARER_TOKEN_TYPE, dateils.getTokenValue()));
        }
    }


}
