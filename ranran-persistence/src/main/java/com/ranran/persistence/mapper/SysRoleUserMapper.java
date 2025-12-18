package com.ranran.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ranran.common.domain.entity.SysRoleUser;
import com.ranran.common.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * @author ranran
 * 角色用户Mapper
 */
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

    public IPage<SysUser> selectUnallocatedPage(IPage<SysUser> page, @Param("roleId") Long roleId);

}
