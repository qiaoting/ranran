package com.ranran.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranran.common.domain.entity.SysRoleUser;
import com.ranran.common.domain.entity.SysUser;
import com.ranran.common.utils.CollUtil;
import com.ranran.common.utils.ObjUtil;
import com.ranran.persistence.mapper.SysRoleUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ranran
 * 角色用户Service
 */
@Service
public class SysRoleUserService extends ServiceImpl<SysRoleUserMapper, SysRoleUser> {

    public boolean hasUsers(Long roleId) {
        LambdaQueryWrapper<SysRoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleUser::getRoleId, roleId);
        return exists(queryWrapper);
    }

    public boolean isUserInUse(Long userId) {
        LambdaQueryWrapper<SysRoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleUser::getUserId, userId);
        return exists(queryWrapper);
    }

    public List<Long> selectUserListByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleUser> queryWrapper = new LambdaQueryWrapper<SysRoleUser>()
                .eq(SysRoleUser::getRoleId, roleId);
        List<SysRoleUser> userRoleList = baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(userRoleList)) {
            return List.of();
        }
        return userRoleList.stream()
                .map(SysRoleUser::getUserId)
                .distinct()
                .toList();
    }

    public boolean deleteUserRole(Long roleId, List<Long> userIds) {
        if (ObjUtil.isNull(roleId) || CollUtil.isEmpty(userIds)) {
            return false;
        }
        LambdaQueryWrapper<SysRoleUser> queryWrapper = new LambdaQueryWrapper<SysRoleUser>()
                .eq(SysRoleUser::getRoleId, roleId)
                .in(SysRoleUser::getUserId, userIds);
        int deleteCount = baseMapper.delete(queryWrapper);
        return deleteCount > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean appendUsers(Long roleId, List<Long> userIds) {
        try {
            if (CollUtil.isEmpty(userIds)) {
                return true;
            }
            List<SysRoleUser> userRoleList = userIds.stream()
                    .map(userId -> {
                        SysRoleUser userRole = new SysRoleUser();
                        userRole.setRoleId(roleId);
                        userRole.setUserId(userId);
                        return userRole;
                    }).collect(Collectors.toList());
            return saveBatch(userRoleList);
        } catch (Exception e) {
            log.error("分配用户失败：", e);
            return false;
        }
    }

    public IPage<SysUser> getUnallocatedPage(IPage page, Long roleId) {
        return baseMapper.selectUnallocatedPage(page, roleId);
    }

}
