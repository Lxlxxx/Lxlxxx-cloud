package com.example.system.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.core.constant.SecurityConstants;
import com.example.core.domain.R;
import com.example.system.api.factory.RemoteUserFallbackFactory;
import com.example.system.api.model.UserInfo;

/**
 * @Author lixianglong
 * @create 2022/8/15 下午12:08
 */
@FeignClient(contextId = "remoteUserService",value = "Lxlxxx-system",fallbackFactory = RemoteUserFallbackFactory.class )
public interface RemoteUserService {

    /**
     * 通过用户名查询用户信息
     * @param username
     * @return
     */
    @GetMapping(value = "/user/info/{username}")
    public R<UserInfo> getUserInfo(@PathVariable("username") String username,
                                   @RequestHeader(SecurityConstants.FROM) String from);
}
