package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UmsMemberQueryParam {

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "帐号启用状态:0->禁用；1->启用")
    private Integer status;
}
