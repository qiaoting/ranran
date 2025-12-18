package com.ranran.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranran.common.domain.entity.SysUser;
import com.ranran.common.utils.ObjUtil;
import com.ranran.persistence.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 * @author ranran
 * 系统用户Service
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    public boolean canAdd(SysUser sysUser) {
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, sysUser.getUsername())
                .last("limit 1");
        SysUser dbUser = getOne(queryWrapper);
        return ObjUtil.isNull(dbUser);
    }

    public boolean canUpdate(SysUser sysUser) {
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, sysUser.getUsername())
                .last("limit 1");
        SysUser dbUser = getOne(queryWrapper);
        if (ObjUtil.isNull(dbUser)) {
            return true;
        } else if (dbUser.getUserId().equals(dbUser.getUserId())) {
            return true;
        }
        return false;
    }

    public SysUser getUserById(Long userId) {
        return baseMapper.selectUserById(userId);
    }

    public SysUser getUserByUsername(String username) {
        return baseMapper.selectUserByUsername(username);
    }

}
