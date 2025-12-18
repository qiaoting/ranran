package com.ranran.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranran.common.domain.entity.SysUser;

/**
 * @author ranran
 * 系统用户Mapper
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    public SysUser selectUserById(Long userId);

    public SysUser selectUserByUsername(String userName);

}
    