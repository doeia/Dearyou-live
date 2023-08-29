package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
public class CmsRegionsParam {

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "拼写")
    @NotEmpty(message = "拼写不能为空")
    private String spell;

    @ApiModelProperty(value = "简拼")
    @NotEmpty(message = "简拼不能为空")
    private String shortSpell;

    @ApiModelProperty(value = "父id")
    @Min(value = 0L, message = "父id错误")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    private Integer displayOrder;

    @ApiModelProperty(value = "版本")
    private Integer ver;
}
