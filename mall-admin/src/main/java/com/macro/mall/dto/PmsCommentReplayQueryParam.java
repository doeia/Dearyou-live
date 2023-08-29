package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PmsCommentReplayQueryParam {
    private Long commentId;

    private String memberNickName;

    @ApiModelProperty(value = "评论人员类型；0->会员；1->管理员")
    private Integer type;
}
