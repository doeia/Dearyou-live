package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CmsRegionsNode {

    @ApiModelProperty(value = "区域id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "级别")
    private Byte layer;

    private List<CmsRegionsNode> children;
}
