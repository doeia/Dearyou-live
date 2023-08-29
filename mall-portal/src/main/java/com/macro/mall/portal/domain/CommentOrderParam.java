package com.macro.mall.portal.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 评价订单参数
 */
@Getter
@Setter
public class CommentOrderParam {
    private Long id;

    @ApiModelProperty(value = "评论内容")
    private String content;
}
