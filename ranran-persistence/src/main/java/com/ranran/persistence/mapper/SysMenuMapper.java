package com.ranran.persistence.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranran.common.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author ranran
 * 菜单Mapper
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    public Set<String> selectPermissionsByRole(@Param("roleIdSet") Set<Long> roleIdSet);

    public List<SysMenu> selectAllMenu(SysMenu sysMenu);

    public List<SysMenu> selectRoutes();

    public List<SysMenu> selectRoutesByUserId(@Param("userId") Long userId);

}
    