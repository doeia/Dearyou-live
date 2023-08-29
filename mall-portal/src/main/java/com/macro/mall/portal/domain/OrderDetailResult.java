package com.macro.mall.portal.domain;

import com.macro.mall.model.OmsOrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 包含订单商品信息的订单详情
 * Created by macro on 2018/9/4.
 */
@Data
public class OrderDetailResult implements Serializable {

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

    @ApiModelProperty(value = "优惠抵扣金额")
    private BigDecimal calAmount;

    @ApiModelProperty(value = "运费金额")
    private BigDecimal freightAmount;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "省份/直辖市")
    private String receiverProvince;

    @ApiModelProperty(value = "城市")
    private String receiverCity;

    @ApiModelProperty(value = "区")
    private String receiverRegion;

    @ApiModelProperty(value = "快递公司")
    private String deliveryCompany;

    @ApiModelProperty(value = "快递单号")
    private String deliverySn;

    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;

    @ApiModelProperty(value = "订单备注")
    private String note;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "是否评价")
    private Boolean isCommnet;

    @ApiModelProperty(value = "订单商品列表")
    private List<OrderItemDetailResult> orderDetailItemList;
}
