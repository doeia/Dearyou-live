package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PmsCommentQueryParam {
    private Long productId;

    private String memberNickName;

    private String productName;

    @ApiModelProperty(value = "订单Id")
    private Long orderId;
}
