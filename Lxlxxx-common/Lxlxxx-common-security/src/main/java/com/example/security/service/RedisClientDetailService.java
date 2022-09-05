package com.example.security.service;


import javax.sql.DataSource;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import com.example.core.constant.CacheConstants;
import com.example.core.constant.SecurityConstants;


/**
 * @Author lixianglong
 * @create 2022/7/30 上午11:56
 */
public class RedisClientDetailService extends JdbcClientDetailsService {

    public RedisClientDetailService(DataSource dataSource) {
        super(dataSource);
        super.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        super.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);

    }

    @Override
    @Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        return super.loadClientByClientId(clientId);
    }
}
