package com.ranran.common.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @author ranran
 */
@Data
public class RouteVo {

    @JsonIgnore
    private Long routeId;

    @JsonIgnore
    private Long parentId;

    @JsonIgnore
    private Integer sort;

    private String name;

    private String path;

    private boolean show = true;

    private String redirect;

    private String component;

    private Meta meta;

    private List<RouteVo> children;
}
