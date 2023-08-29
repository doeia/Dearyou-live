package com.macro.mall.portal.domain;

import com.macro.mall.model.OmsOrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单列表详情返回信息封装
 */
@Getter
@Setter
public class OrderResult implements Serializable {

    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "应付金额（实际支付金额）")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "创建时间")
    private String createTimeDesc;

    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;

    @ApiModelProperty(value = "订单状态")
    private String statusDesc;

    @ApiModelProperty(value = "是否评价")
    private Boolean isCommnet;

    @ApiModelProperty(value = "订单商品列表")
    private List<OmsOrderItem> orderDetailItemList;
}
