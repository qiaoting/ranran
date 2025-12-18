package com.ranran.framework.web.service;

import com.ranran.common.constant.Constant;
import com.ranran.common.domain.vo.LoginUser;
import com.ranran.common.utils.CollUtil;
import com.ranran.common.utils.ObjUtil;
import com.ranran.common.utils.SecurityUtil;
import com.ranran.common.utils.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author ranran
 * 自定义权限判断
 */
@Service("pcs")
public class PermissionCheckService {

    public boolean hasPermission(String permission) {
        if (!StrUtil.hasText(permission)) {
            return false;
        }
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (ObjUtil.isNull(loginUser) || CollUtil.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermission(loginUser.getPermissions(), permission);
    }

    private boolean hasPermission(Set<String> permissions, String permission) {
        return permissions.contains(Constant.ADMIN_PERMISSION)
                || permissions.contains(StrUtil.trim(permission));
    }

}
