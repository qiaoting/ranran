package com.ranran.common.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ranran
 * 分页数据
 */
@Data
public class PageVo implements java.io.Serializable {

    private long total;

    private List<?> rows;

    private int code;

    private String msg;

}
