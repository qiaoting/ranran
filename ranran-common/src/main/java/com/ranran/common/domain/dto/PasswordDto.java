package com.ranran.common.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author ranran
 * 修改密码参数
 */
@Data
public class PasswordDto {

    private Long userId;
    private String oldPassword;
    @NotNull
    private String newPassword;

}
