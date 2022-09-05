package com.example.security.login;

import lombok.Data;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @Author lixianglong
 * @create 2022/7/30 下午1:17
 */
@Data
public class LoginUser extends User {

    private Long userId;


    public LoginUser(Long userId,String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId=userId;
    }

}
