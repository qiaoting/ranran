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
 * 公告实体类
 */
@Data
@TableName("sys_announcement")
public class SysAnnouncement {

    @TableId(type = IdType.AUTO)
    private Long announcementId;
    private String title;
    private String content;
    private String announcementType;
    private String status;
    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;

    @JsonFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;
}
