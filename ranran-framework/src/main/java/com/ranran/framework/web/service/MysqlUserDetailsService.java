package com.ranran.framework.web.service;

import com.ranran.common.constant.Constant;
import com.ranran.common.domain.entity.SysRole;
import com.ranran.common.domain.entity.SysUser;
import com.ranran.common.domain.vo.LoginUser;
import com.ranran.common.exception.BusinessException;
import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.SecurityUtil;
import com.ranran.persistence.service.impl.SysMenuService;
import com.ranran.persistence.service.impl.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ranran
 * 自定义用户查询
 */
@Service
@Slf4j
public class MysqlUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserByUsername(username);
        if (ObjUtil.isNull(sysUser)) {
            log.info("登录用户：{} 不存在.", username);
            throw new BusinessException("用户不存在");
        }
        List<String> roleCodeList = sysUser.getRoles().stream().map(SysRole::getRoleCode).toList();
        Set<Long> roleIdSet = sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toSet());
        Set<String> permissionList;
        if (SecurityUtil.isAdmin(sysUser.getUserId())) {
            permissionList = new HashSet<String>(Set.of(Constant.ADMIN_PERMISSION));
        } else {
            permissionList = sysMenuService.getPermissionsByRole(roleIdSet);
        }
        return new LoginUser(
                sysUser.getUserId(),
                sysUser.getUsername(),
                sysUser.getPassword(),
                roleCodeList,
                permissionList
        );
    }
}