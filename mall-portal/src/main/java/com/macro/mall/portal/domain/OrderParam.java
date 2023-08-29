package com.macro.mall.portal.domain;

import com.macro.mall.model.OmsCartItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 生成订单时传入的参数
 * Created by macro on 2018/8/30.
 */
@Getter
@Setter
public class OrderParam {
    //选中的购物车商品Id
    private List<Long> cartIds;
    //收货地址id
    private Long memberReceiveAddressId;
    //优惠券id
    private Long couponId;
    //使用的积分数
    private Integer useIntegration;
    //支付方式
    private Integer payType;
    //订单备注
    private String note;

    @ApiModelProperty(value = "是否立即购买")
    private Boolean isBuyNow;

    @ApiModelProperty(value = "购物车项")
    private OmsCartItem cartItem;
}
