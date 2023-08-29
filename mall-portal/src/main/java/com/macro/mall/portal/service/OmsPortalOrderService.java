package com.macro.mall.portal.service;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.OmsCartItem;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.portal.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 前台订单管理Service
 * Created by macro on 2018/8/30.
 */
public interface OmsPortalOrderService {
    /**
     * 根据用户购物车信息生成确认单信息
     */
    ConfirmOrderResult generateConfirmOrder();

    /**
     * 根据用户购物车信息生成确认单信息
     */
    ConfirmOrderResult generateConfirmOrder(ConfirmOrderParam confirmOrderParam);

    /**
     * 根据提交信息生成订单
     */
    @Transactional
    CommonResult<WxPayMpOrderResult> generateOrder(OrderParam orderParam) throws WxPayException;

    /**
     * 重新支付订单
     */
    CommonResult repayOrder(Long orderId) throws WxPayException;

    /**
     * 支付成功后的回调
     */
    @Transactional
    CommonResult paySuccess(Long orderId);

    /**
     * 线上支付成功后的回调
     */
    @Transactional
    CommonResult onlinePaySuccess(Long orderId, String paySn);

    /**
     * 自动取消超时订单
     */
    @Transactional
    CommonResult cancelTimeOutOrder();

    /**
     * 取消单个超时订单
     */
    @Transactional
    CommonResult cancelOrder(Long orderId);

    /**
     * 确认收货单个订单
     */
    @Transactional
    CommonResult receiveOrder(Long orderId);

    /**
     * 评价单个订单
     */
    @Transactional
    CommonResult commentOrder(CommentOrderParam commentOrderParam);

    /**
     * 发送延迟消息取消订单
     */
    void sendDelayMessageCancelOrder(Long orderId);

    /**
     * 根据订单编号获取订单
     */
    OmsOrder getOrderBySn(String orderSn);

    /**
     * 获取当前会员的订单列表
     */
    List<OrderResult> getOrderList(OmsOrderParam queryParam);

    /**
     * 获取订单详情
     */
    OrderDetailResult getOrderDetail(Long id);
}
