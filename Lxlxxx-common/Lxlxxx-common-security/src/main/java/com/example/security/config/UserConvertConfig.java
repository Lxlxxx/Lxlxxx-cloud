package com.example.security.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import com.example.core.constant.SecurityConstants;
import com.example.core.text.Convert;
import com.example.security.login.LoginUser;

/**
 * @Author lixianglong
 * @create 2022/8/19 上午9:37
 */
public class UserConvertConfig implements UserAuthenticationConverter {

    private static final String N_A = "N/A";

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {

        Map<String, Object> authMap = new LinkedHashMap<>();
        authMap.put(USERNAME, userAuthentication.getName());

        //如果取出来的权限用户信息不为空
        if (userAuthentication.getAuthorities() != null && !userAuthentication.getAuthorities().isEmpty()) {
            //Authorities信息list转set
            authMap.put(AUTHORITIES, AuthorityUtils.authorityListToSet(userAuthentication.getAuthorities()));
        }
        return authMap;

    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.get(AUTHORITIES) instanceof String
        ){
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

            Long userId = Convert.toLong(map.get(SecurityConstants.DETAILS_USERID));
            String username = (String) map.get(SecurityConstants.DETAILS_USERNAME);
            LoginUser user = new LoginUser(userId, username, N_A, true, true, true, true, authorities);
            return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
        }
            return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map){

        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String)
        {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection)
        {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(
                    StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }

    }
}
