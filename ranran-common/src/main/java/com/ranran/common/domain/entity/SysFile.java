package com.ranran.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ranran.common.constant.DateConstant;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ranran
 * 文件实体类
 */
@Data
@TableName("sys_file")
public class SysFile {

    @TableId(type = IdType.AUTO)
    private Long fileId;
    private String fileName;
    private String fileUrl;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String createBy;

    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;

    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;

}
