package com.example.auth.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.example.core.constant.CacheConstants;
import com.example.core.constant.SecurityConstants;
import com.example.security.login.LoginUser;
import com.example.security.service.RedisClientDetailService;

/**
 * @Author lixianglong
 * @create 2022/7/23 下午5:20
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig implements AuthorizationServerConfigurer {

    @Autowired
    private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 配置令牌的安全约束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()");
    }

    /**
     * 配置客户端详情
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.withClientDetails(clientDetailService());
    }

    /**
     * 定义授权、令牌、令牌服务
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.
                //请求方式
                        allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                //token存在redis
                .tokenStore(tokenStore())
                //令牌token的生成方式
                .tokenEnhancer(tokenEnhancer())
                //用户账号密码认证
                .userDetailsService(userDetailsService)
                //认证管理器
                .authenticationManager(authenticationManager)
                //不允许重复使用refresh_token
                .reuseRefreshTokens(false)
                //使用原生态异常
                .exceptionTranslator(exceptionTranslator);
    }

    /**
     * 通过redis来获取client
     *
     * @return
     */
    public RedisClientDetailService clientDetailService() {
        return new RedisClientDetailService(dataSource);

    }

    /**
     * 基于 Redis 实现，令牌保存到缓存
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(CacheConstants.OAUTH_ACCESS);
        return tokenStore;
    }

    /**
     * 自定义生成令牌 通过sys_user表 userId和userName生成自定token
     *
     * @return
     */
    public TokenEnhancer tokenEnhancer() {
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                if (accessToken instanceof DefaultOAuth2AccessToken) {
                    DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
                    LoginUser loginUser = (LoginUser) authentication.getUserAuthentication().getPrincipal();
                    Map<String, Object> additionalInformation = new LinkedHashMap<>();
                    additionalInformation.put(SecurityConstants.DETAILS_USERID, loginUser.getUserId());
                    additionalInformation.put(SecurityConstants.DETAILS_USERNAME, authentication.getName());
                    token.setAdditionalInformation(additionalInformation);
                }
                return accessToken;
            }
        };
    }

}
