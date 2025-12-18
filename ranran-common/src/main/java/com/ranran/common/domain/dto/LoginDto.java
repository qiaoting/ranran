package com.ranran.common.domain.dto;

import lombok.Data;

/**
 * @author ranran
 * 登录参数
 */
@Data
public class LoginDto {

    private String username;
    private String password;
    private String code;
    private String codeToken;

}
