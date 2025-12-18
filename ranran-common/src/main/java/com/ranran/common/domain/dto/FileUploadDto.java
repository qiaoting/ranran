package com.ranran.common.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ranran
 * 文件上传参数
 */
@Data
public class FileUploadDto {
    private MultipartFile file;
}
