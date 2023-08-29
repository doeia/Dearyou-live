package com.macro.mall.portal.domain;

import com.macro.mall.model.OmsCartItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 订单查询参数
 */
@Getter
@Setter
public class ConfirmOrderParam {
    @ApiModelProperty(value = "选择的购物车Id列表")
    private List<Long> cartIds;

    @ApiModelProperty(value = "选择的收货地址Id")
    private Long addressId;

    @ApiModelProperty(value = "是否立即购买")
    private Boolean isBuyNow;

    @ApiModelProperty(value = "购物车项")
    private OmsCartItem cartItem;
}
