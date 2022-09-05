package com.example.system.api.model;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.example.system.api.domain.SysRole;
import com.example.system.api.domain.SysUser;

/**
 * @Author lixianglong
 * @create 2022/8/15 下午12:00
 */
@Data
public class UserInfo implements Serializable {

    /**
     * 用户信息
     */
    private SysUser sysUser;

    /**
     * 权限标识集合
     */
    private Set<String> permissions;

    /**
     * 角色集合
     */
    private Set<String> roles;
    
}
