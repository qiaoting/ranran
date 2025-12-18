package com.ranran.common.domain.dto;

import lombok.Data;

/**
 * @author ranran
 * 角色用户查询参数
 */
@Data
public class RoleUserQueryDto {
    private Long roleId;
    private String username;
    private String nickname;

}
