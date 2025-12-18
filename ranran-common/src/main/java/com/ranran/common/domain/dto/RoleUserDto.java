package com.ranran.common.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author ranran
 * 角色绑定参数
 */
@Data
public class RoleUserDto {

    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    @NotNull(message = "用户集合不能为空")
    private List<Long> userIds;
}
