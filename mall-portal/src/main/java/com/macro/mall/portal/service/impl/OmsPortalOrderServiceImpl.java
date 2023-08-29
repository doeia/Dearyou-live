package com.macro.mall.portal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageHelper;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.*;
import com.macro.mall.model.*;
import com.macro.mall.portal.component.CancelOrderSender;
import com.macro.mall.portal.dao.PortalOrderDao;
import com.macro.mall.portal.dao.PortalOrderItemDao;
import com.macro.mall.portal.dao.PortalProductDao;
import com.macro.mall.portal.dao.SmsCouponHistoryDao;
import com.macro.mall.portal.domain.*;
import com.macro.mall.portal.service.*;
import com.macro.mall.portal.util.DataValidateUtil;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 前台订单管理Service
 * Created by macro on 2018/8/30.
 */
@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberStatisticsInfoMapper memberStatisticsInfoMapper;
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;
    @Autowired
    private UmsMemberCouponService memberCouponService;
    @Autowired
    private UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private SmsCouponHistoryDao couponHistoryDao;
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private PortalOrderItemDao orderItemDao;
    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;
    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.orderId}")
    private String REDIS_KEY_PREFIX_ORDER_ID;
    @Autowired
    private PmsProductMapper pmsProductMapper;
    @Autowired
    private PortalProductDao portalProductDao;
    @Autowired
    private PortalOrderDao portalOrderDao;
    @Autowired
    private OmsOrderSettingMapper orderSettingMapper;
    @Autowired
    private OmsOrderItemMapper orderItemMapper;
    @Autowired
    private CancelOrderSender cancelOrderSender;
    @Autowired
    private WxPayService wxService;
    @Autowired
    private PmsCommentMapper pmsCommentMapper;
    @Autowired
    private OmsOrderReturnApplyMapper returnApplyMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ConfirmOrderResult generateConfirmOrder() {
        ConfirmOrderResult result = new ConfirmOrderResult();
        //获取购物车信息
        UmsMember currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId());
        result.setCartPromotionItemList(cartPromotionItemList);
        //获取用户收货地址列表
        List<UmsMemberReceiveAddress> memberReceiveAddressList = memberReceiveAddressService.list();
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        //获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        //获取用户积分
        result.setMemberIntegration(currentMember.getIntegration());
        //获取积分使用规则
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
        result.setIntegrationConsumeSetting(integrationConsumeSetting);
        //计算总金额、活动优惠、应付金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    @Override
    public ConfirmOrderResult generateConfirmOrder(ConfirmOrderParam confirmOrderParam) {
        ConfirmOrderResult result = new ConfirmOrderResult();
        //获取购物车信息
        UmsMember currentMember = memberService.getCurrentMember();
        result.setIdCard(currentMember.getIdCard());
        result.setRealName(currentMember.getRealName());

        //如果为立即购买则获取请求参数的购物车信息，否则获取选中的购物车列表
        List<CartPromotionItem> cartPromotionItemList;
        if (confirmOrderParam.getIsBuyNow() != null && confirmOrderParam.getIsBuyNow()) {
            ArrayList<OmsCartItem> cartItemList = new ArrayList<>();
            cartItemList.add(confirmOrderParam.getCartItem());
            cartPromotionItemList = cartItemService.listPromotion(cartItemList);
        } else {
            cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), confirmOrderParam.getCartIds());
        }
        result.setCartPromotionItemList(cartPromotionItemList);

        //如果有选中的收货地址则拿选中的地址，否则拿默认收货地址
        UmsMemberReceiveAddress memberReceiveAddressList;
        if (confirmOrderParam.getAddressId() != null && confirmOrderParam.getAddressId() > 0) {
            //获取用户选中的收货地址
            memberReceiveAddressList = memberReceiveAddressService.getItem(confirmOrderParam.getAddressId());
        } else {
            //获取用户默认收货地址
            memberReceiveAddressList = memberReceiveAddressService.defaultAddress();
        }
        result.setMemberReceiveAddress(memberReceiveAddressList);

        //获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        //获取用户积分
        result.setMemberIntegration(currentMember.getIntegration());
        //获取积分使用规则
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
        result.setIntegrationConsumeSetting(integrationConsumeSetting);
        //计算总金额、活动优惠、应付金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    @Override
    public CommonResult<WxPayMpOrderResult> generateOrder(OrderParam orderParam) throws WxPayException {
        //获取购物车及优惠信息
        UmsMember currentMember = memberService.getCurrentMember();

        //验证身份证和真实姓名
        if (StringUtils.isEmpty(currentMember.getRealName())) {
            return CommonResult.failed("请填写真实姓名");
        }
        if (StringUtils.isEmpty(currentMember.getIdCard())) {
            return CommonResult.failed("请填写身份证号码");
        }

        //验证收货地址
        if (orderParam.getMemberReceiveAddressId() == null || orderParam.getMemberReceiveAddressId() == 0) {
            return CommonResult.failed("请选择收货地址");
        }

        //如果为立即购买则获取请求参数的购物车信息，否则获取选中的购物车列表
        List<CartPromotionItem> cartPromotionItemList;
        if (orderParam.getIsBuyNow() != null && orderParam.getIsBuyNow()) {
            ArrayList<OmsCartItem> cartItemList = new ArrayList<>();
            cartItemList.add(orderParam.getCartItem());
            cartPromotionItemList = cartItemService.listPromotion(cartItemList);
        } else {
            cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), orderParam.getCartIds());
        }

        List<OmsOrderItem> orderItemList = new ArrayList<>();
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            //生成下单商品信息
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(cartPromotionItem.getProductId());
            orderItem.setProductName(cartPromotionItem.getProductName());
            orderItem.setProductPic(cartPromotionItem.getProductPic());
            orderItem.setProductAttr(cartPromotionItem.getProductAttr());
            orderItem.setProductBrand(cartPromotionItem.getProductBrand());
            orderItem.setProductSn(cartPromotionItem.getProductSn());
            orderItem.setProductPrice(cartPromotionItem.getPrice());
            orderItem.setProductQuantity(cartPromotionItem.getQuantity());
            orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
            orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
            orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
            orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
            orderItem.setGiftGrowth(cartPromotionItem.getGrowth());
            orderItem.setSp1(cartPromotionItem.getSp1());
            orderItem.setSp2(cartPromotionItem.getSp2());
            orderItem.setSp3(cartPromotionItem.getSp3());
            orderItem.setProductAttr(cartPromotionItem.getProductAttr());
            orderItemList.add(orderItem);
        }

        //判断订单的商品是否可以购买
        String canNoBuyProduct = orderProductIsCanBuy(orderItemList);
        if (!StringUtils.isEmpty(canNoBuyProduct)) {
            return CommonResult.failed(canNoBuyProduct);
        }

        //判断购物车中商品是否都有库存
        if (!hasStock(cartPromotionItemList)) {
            return CommonResult.failed("库存不足，无法下单");
        }
        //判断使用使用了优惠券
        if (orderParam.getCouponId() == null) {
            //不用优惠券
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setCouponAmount(new BigDecimal(0));
            }
        } else {
            //使用优惠券
            SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.getCouponId());
            if (couponHistoryDetail == null) {
                return CommonResult.failed("该优惠券不可用");
            }
            //对下单商品的优惠券进行处理
            handleCouponAmount(orderItemList, couponHistoryDetail);
        }
        //判断是否使用积分
        if (orderParam.getUseIntegration() == null) {
            //不使用积分
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setIntegrationAmount(new BigDecimal(0));
            }
        } else {
            //使用积分
            BigDecimal totalAmount = calcTotalAmount(orderItemList);
            BigDecimal integrationAmount = getUseIntegrationAmount(orderParam.getUseIntegration(), totalAmount, currentMember, orderParam.getCouponId() != null);
            if (integrationAmount.compareTo(new BigDecimal(0)) == 0) {
                return CommonResult.failed("积分不可用");
            } else {
                //可用情况下分摊到可用商品中
                for (OmsOrderItem orderItem : orderItemList) {
                    BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(integrationAmount);
                    orderItem.setIntegrationAmount(perAmount);
                }
            }
        }
        //计算order_item的实付金额
        handleRealAmount(orderItemList);
        //进行库存锁定
        lockStock(cartPromotionItemList);
        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
        OmsOrder order = new OmsOrder();
        order.setRealName(currentMember.getRealName());
        order.setIdCard(currentMember.getIdCard());
        order.setMemberNickname(currentMember.getNickname());
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcTotalAmount(orderItemList));
        order.setFreightAmount(new BigDecimal(0));
        order.setPromotionAmount(calcPromotionAmount(orderItemList));
        order.setPromotionInfo(getOrderPromotionInfo(orderItemList));
        if (orderParam.getCouponId() == null) {
            order.setCouponAmount(new BigDecimal(0));
        } else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(calcCouponAmount(orderItemList));
        }
        if (orderParam.getUseIntegration() == null) {
            order.setIntegration(0);
            order.setIntegrationAmount(new BigDecimal(0));
        } else {
            order.setIntegration(orderParam.getUseIntegration());
            order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
        }
        order.setPayAmount(calcPayAmount(order));
        //转化为订单信息并插入数据库
        order.setMemberId(currentMember.getId());
        order.setCreateTime(new Date());
        order.setMemberUsername(currentMember.getUsername());
        //支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(orderParam.getPayType());
        //订单来源：0->PC订单；1->app订单
        order.setSourceType(1);
        //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        //订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);
        //收货人信息：姓名、电话、邮编、地址
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        //0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        //计算赠送积分
        order.setIntegration(calcGifIntegration(orderItemList));
        //计算赠送成长值
        order.setGrowth(calcGiftGrowth(orderItemList));
        //设置订单备注
        order.setNote(orderParam.getNote());
        //生成订单号
        order.setOrderSn(generateOrderSn(order));
        // TODO: 2018/9/3 bill_*,delivery_*
        //插入order表和order_item表
        orderMapper.insert(order);
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }
        orderItemDao.insertList(orderItemList);
        //如使用优惠券更新优惠券使用状态
        if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
        }
        //如使用积分需要扣除积分
        if (orderParam.getUseIntegration() != null) {
            order.setUseIntegration(orderParam.getUseIntegration());
            memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - orderParam.getUseIntegration());
        }

        //为立即购买不删除购物车商品
        if (orderParam.getIsBuyNow() == null || !orderParam.getIsBuyNow()) {
            //删除购物车中的下单商品
            deleteCartItemList(cartPromotionItemList, currentMember);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);

        //如果是微信支付则调起微信支付
        if (orderParam.getPayType() == 2) {
            CartPromotionItem firstCartItem = cartPromotionItemList.get(0);
            //商品描述
            String body = firstCartItem.getProductName();
            if (cartPromotionItemList.size() > 1) {
                body += "等多个商品";
            }

            WxPayMpOrderResult wxPayResult = getWxPayJsParams(body, order.getOrderSn(), currentMember.getUsername(), order.getPayAmount());
            return CommonResult.success(wxPayResult, "下单成功");
        } else {
            return CommonResult.failed("下单失败,不支持该支付方式");
        }
    }

    @Override
    public CommonResult repayOrder(Long orderId) throws WxPayException {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        if (order.getStatus() != 0) {
            return CommonResult.failed("重新支付失败,订单不是待付款状态");
        }

        OmsOrderItemExample example = new OmsOrderItemExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        List<OmsOrderItem> orderItems = orderItemMapper.selectByExample(example);

        if (CollectionUtil.isEmpty(orderItems)) {
            return CommonResult.failed("重新支付失败,该订单未包含商品");
        }

        OmsOrderItem firstOrderItem = orderItems.get(0);

        //商品描述
        String body = firstOrderItem.getProductName();
        if (orderItems.size() > 1) {
            body += "等多个商品";
        }

        //当前会员
        UmsMember currentMember = memberService.getCurrentMember();

        WxPayMpOrderResult wxPayResult = getWxPayJsParams(body, order.getOrderSn(), currentMember.getUsername(), order.getPayAmount());
        return CommonResult.success(wxPayResult, "下单成功");
    }

    @Override
    public CommonResult paySuccess(Long orderId) {
        //修改订单支付状态
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);
        //order.setId(orderId);
        order.setStatus(1);
        order.setPaymentTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);

        //修改用户统计信息
        //不存在则新增
        UmsMemberStatisticsInfoExample example = new UmsMemberStatisticsInfoExample();
        example.createCriteria().andMemberIdEqualTo(order.getMemberId());
        List<UmsMemberStatisticsInfo> umsMemberStatisticsInfos = memberStatisticsInfoMapper.selectByExample(example);
        if(umsMemberStatisticsInfos.isEmpty())
        {
            UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
            umsMemberStatisticsInfo.setMemberId(order.getMemberId());
            umsMemberStatisticsInfo.setConsumeAmount(order.getTotalAmount());
            umsMemberStatisticsInfo.setOrderCount(1);
            umsMemberStatisticsInfo.setCouponCount(0);
            umsMemberStatisticsInfo.setCommentCount(0);
            umsMemberStatisticsInfo.setReturnOrderCount(0);
            umsMemberStatisticsInfo.setLoginCount(0);
            umsMemberStatisticsInfo.setAttendCount(0);
            umsMemberStatisticsInfo.setFansCount(0);
            umsMemberStatisticsInfo.setCollectProductCount(0);
            umsMemberStatisticsInfo.setCollectSubjectCount(0);
            umsMemberStatisticsInfo.setCollectTopicCount(0);
            umsMemberStatisticsInfo.setCollectCommentCount(0);
            umsMemberStatisticsInfo.setInviteFriendCount(0);
            umsMemberStatisticsInfo.setRecentOrderTime(new Date());
            memberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
        }else
            {
                UmsMemberStatisticsInfo umsMemberStatisticsInfo = umsMemberStatisticsInfos.get(0);
                umsMemberStatisticsInfo.setConsumeAmount(umsMemberStatisticsInfo.getConsumeAmount().add(order.getTotalAmount()));
                umsMemberStatisticsInfo.setOrderCount(umsMemberStatisticsInfo.getOrderCount() + 1);
                umsMemberStatisticsInfo.setRecentOrderTime(new Date());
                memberStatisticsInfoMapper.updateByPrimaryKeySelective(umsMemberStatisticsInfo);
            }


        //恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderDetail orderDetail = portalOrderDao.getDetail(orderId);
        int count = this.updateProductStock(orderDetail.getOrderItemList());
        return CommonResult.success(count, "支付成功");
    }

    @Override
    public CommonResult onlinePaySuccess(Long orderId, String paySn) {
        //修改订单支付状态
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);
        //order.setId(orderId);
        order.setStatus(1);
        order.setPaymentTime(new Date());
        order.setPaySn(paySn);
        orderMapper.updateByPrimaryKeySelective(order);

        //查询订单信息
        OmsOrder payOrder = orderMapper.selectByPrimaryKey(orderId);

        //修改用户统计信息
        //不存在则新增
        UmsMemberStatisticsInfoExample example = new UmsMemberStatisticsInfoExample();
        example.createCriteria().andMemberIdEqualTo(payOrder.getMemberId());
        List<UmsMemberStatisticsInfo> umsMemberStatisticsInfos = memberStatisticsInfoMapper.selectByExample(example);
        if(umsMemberStatisticsInfos.isEmpty())
        {
            UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
            umsMemberStatisticsInfo.setMemberId(payOrder.getMemberId());
            umsMemberStatisticsInfo.setConsumeAmount(payOrder.getTotalAmount());
            umsMemberStatisticsInfo.setOrderCount(1);
            umsMemberStatisticsInfo.setCouponCount(0);
            umsMemberStatisticsInfo.setCommentCount(0);
            umsMemberStatisticsInfo.setReturnOrderCount(0);
            umsMemberStatisticsInfo.setLoginCount(0);
            umsMemberStatisticsInfo.setAttendCount(0);
            umsMemberStatisticsInfo.setFansCount(0);
            umsMemberStatisticsInfo.setCollectProductCount(0);
            umsMemberStatisticsInfo.setCollectSubjectCount(0);
            umsMemberStatisticsInfo.setCollectTopicCount(0);
            umsMemberStatisticsInfo.setCollectCommentCount(0);
            umsMemberStatisticsInfo.setInviteFriendCount(0);
            umsMemberStatisticsInfo.setRecentOrderTime(new Date());
            memberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
        }else
        {
            UmsMemberStatisticsInfo umsMemberStatisticsInfo = umsMemberStatisticsInfos.get(0);
            umsMemberStatisticsInfo.setConsumeAmount(umsMemberStatisticsInfo.getConsumeAmount().add(payOrder.getTotalAmount()));
            umsMemberStatisticsInfo.setOrderCount(umsMemberStatisticsInfo.getOrderCount() + 1);
            umsMemberStatisticsInfo.setRecentOrderTime(new Date());
            memberStatisticsInfoMapper.updateByPrimaryKeySelective(umsMemberStatisticsInfo);
        }

        //恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderDetail orderDetail = portalOrderDao.getDetail(orderId);
        int count = this.updateProductStock(orderDetail.getOrderItemList());
        return CommonResult.success(count, "支付成功");
    }

    @Override
    public CommonResult cancelTimeOutOrder() {
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        //查询超时、未支付的订单及订单详情
        List<OmsOrderDetail> timeOutOrders = portalOrderDao.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
        if (CollectionUtils.isEmpty(timeOutOrders)) {
            return CommonResult.failed("暂无超时订单");
        }
        //修改订单状态为交易取消
        List<Long> ids = new ArrayList<>();
        for (OmsOrderDetail timeOutOrder : timeOutOrders) {
            ids.add(timeOutOrder.getId());
        }
        portalOrderDao.updateOrderStatus(ids, 4);
        for (OmsOrderDetail timeOutOrder : timeOutOrders) {

            //获取有规格的订单信息表
            List<OmsOrderItem> allOrderItems = timeOutOrder.getOrderItemList();

            //释放商品锁定库存-区分有无规格
            releaseProductStockLock(allOrderItems);

            //修改优惠券使用状态
            updateCouponStatus(timeOutOrder.getCouponId(), timeOutOrder.getMemberId(), 0);
            //返还使用积分
            if (timeOutOrder.getUseIntegration() != null) {
                UmsMember member = memberService.getById(timeOutOrder.getMemberId());
                memberService.updateIntegration(timeOutOrder.getMemberId(), member.getIntegration() + timeOutOrder.getUseIntegration());
            }
        }
        return CommonResult.success(null);
    }

    @Override
    public CommonResult cancelOrder(Long orderId) {
        //查询未付款的取消订单
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(orderId).andStatusEqualTo(0).andDeleteStatusEqualTo(0);
        List<OmsOrder> cancelOrderList = orderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(cancelOrderList)) {
            return CommonResult.failed("找不到该订单");
        }
        OmsOrder cancelOrder = cancelOrderList.get(0);
        if (cancelOrder != null) {
            //修改订单状态为取消
            cancelOrder.setStatus(4);
            int result = orderMapper.updateByPrimaryKeySelective(cancelOrder);
            OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(orderId);
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            //解除订单商品库存锁定
            if (!CollectionUtils.isEmpty(orderItemList)) {
                //释放商品锁定库存-区分有无规格
                releaseProductStockLock(orderItemList);
            }
            //修改优惠券使用状态
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            //返还使用积分
            if (cancelOrder.getUseIntegration() != null) {
                UmsMember member = memberService.getById(cancelOrder.getMemberId());
                memberService.updateIntegration(cancelOrder.getMemberId(), member.getIntegration() + cancelOrder.getUseIntegration());
            }
            return result > 0 ? CommonResult.success("取消成功") : CommonResult.failed("取消失败");
        }
        return CommonResult.failed("找不到该订单");
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        //获取订单超时时间
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
        //发送延迟消息
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    @Override
    public CommonResult receiveOrder(Long orderId) {
        //查询待收货(已发货)的订单
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(orderId).andStatusEqualTo(2).andDeleteStatusEqualTo(0);
        List<OmsOrder> receiveOrderList = orderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(receiveOrderList)) {
            return CommonResult.failed("找不到该订单");
        }
        OmsOrder receiveOrder = receiveOrderList.get(0);
        if (receiveOrder != null) {
            //修改订单状态为已完成
            receiveOrder.setStatus(3);
            receiveOrder.setReceiveTime(new Date());
            Integer result = orderMapper.updateByPrimaryKeySelective(receiveOrder);
            return result > 0 ? CommonResult.success("收货成功") : CommonResult.failed("收货失败");
        }
        return CommonResult.failed("找不到该订单");
    }

    @Override
    public CommonResult commentOrder(CommentOrderParam commentOrderParam) {
        //判断该订单是否已评价
        if (orderIsComment(commentOrderParam.getId())) {
            return CommonResult.failed("该订单已评价");
        }

        //查询待评价(已完成)的订单
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(commentOrderParam.getId()).andStatusEqualTo(3).andDeleteStatusEqualTo(0);
        List<OmsOrder> commentOrderList = orderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(commentOrderList)) {
            return CommonResult.failed("找不到该订单");
        }

        OmsOrder commentOrder = commentOrderList.get(0);
        if (commentOrder != null) {
            //查找订单下的商品
            OmsOrderItemExample itemExample = new OmsOrderItemExample();
            itemExample.createCriteria().andOrderIdEqualTo(commentOrderParam.getId());
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(itemExample);

            //获取当前用户
            UmsMember currentMember = memberService.getCurrentMember();

            //添加商品评价
            Integer result = 0;
            for (OmsOrderItem orderItem : orderItemList) {
                PmsComment comment = new PmsComment();
                comment.setContent(commentOrderParam.getContent());
                comment.setProductId(orderItem.getProductId());
                comment.setProductName(orderItem.getProductName());
                comment.setProductName(orderItem.getProductName());
                comment.setMemberNickName(currentMember.getNickname());
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                comment.setMemberIp(request.getRemoteAddr());
                comment.setMemberIcon(currentMember.getIcon());
                comment.setCreateTime(new Date());
                comment.setShowStatus(1);
                comment.setOrderId(commentOrderParam.getId());
                comment.setProductAttribute(orderItem.getProductAttr());
                Integer addResult = pmsCommentMapper.insert(comment);
                if (addResult > 0) {
                    result++;
                }
            }

            commentOrder.setCommentTime(new Date());
            result += orderMapper.updateByPrimaryKeySelective(commentOrder);

            if(result > 0)
            {
                //修改用户统计信息
                //不存在则新增
                UmsMemberStatisticsInfoExample examples = new UmsMemberStatisticsInfoExample();
                examples.createCriteria().andMemberIdEqualTo(commentOrder.getMemberId());
                List<UmsMemberStatisticsInfo> umsMemberStatisticsInfos = memberStatisticsInfoMapper.selectByExample(examples);
                if(umsMemberStatisticsInfos.isEmpty())
                {
                    UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
                    umsMemberStatisticsInfo.setMemberId(commentOrder.getMemberId());
                    umsMemberStatisticsInfo.setConsumeAmount(new BigDecimal(0));
                    umsMemberStatisticsInfo.setOrderCount(0);
                    umsMemberStatisticsInfo.setCouponCount(0);
                    umsMemberStatisticsInfo.setCommentCount(1);
                    umsMemberStatisticsInfo.setReturnOrderCount(0);
                    umsMemberStatisticsInfo.setLoginCount(0);
                    umsMemberStatisticsInfo.setAttendCount(0);
                    umsMemberStatisticsInfo.setFansCount(0);
                    umsMemberStatisticsInfo.setCollectProductCount(0);
                    umsMemberStatisticsInfo.setCollectSubjectCount(0);
                    umsMemberStatisticsInfo.setCollectTopicCount(0);
                    umsMemberStatisticsInfo.setCollectCommentCount(0);
                    umsMemberStatisticsInfo.setInviteFriendCount(0);
                    memberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
                }else
                {
                    UmsMemberStatisticsInfo umsMemberStatisticsInfo = umsMemberStatisticsInfos.get(0);
                    umsMemberStatisticsInfo.setCommentCount(umsMemberStatisticsInfo.getCommentCount() + 1);
                    memberStatisticsInfoMapper.updateByPrimaryKeySelective(umsMemberStatisticsInfo);
                }
            }

            return result > 0 ? CommonResult.success("评价成功") : CommonResult.failed("评价失败");
        }
        return CommonResult.failed("找不到该订单");
    }

    @Override
    public OmsOrder getOrderBySn(String orderSn) {
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andOrderSnEqualTo(orderSn);
        return orderMapper.selectByExample(example).get(0);
    }

    @Override
    public List<OrderResult> getOrderList(OmsOrderParam queryParam) {
        PageHelper.startPage(queryParam.getPage().getPageNum(), queryParam.getPage().getPageSize());
        Long memberId = memberService.getCurrentMember().getId();
        List<OrderResult> orderResultList = portalOrderDao.getOrderList(memberId, queryParam);

        //已完成订单是否评价
        for (OrderResult order : orderResultList) {
            if (order.getStatus() == 3) {
                Boolean isCommnet = orderIsComment(order.getId());
                order.setIsCommnet(isCommnet);
            } else {
                order.setIsCommnet(false);
            }
        }

        return orderResultList;
    }

    @Override
    public OrderDetailResult getOrderDetail(Long id) {
        OrderDetailResult orderDetailResult = portalOrderDao.getOrderDetail(id);
        Boolean isCommnet = orderIsComment(id);
        orderDetailResult.setIsCommnet(isCommnet);
        orderProductIsAfterSale(orderDetailResult.getOrderDetailItemList());
        return orderDetailResult;
    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = REDIS_KEY_PREFIX_ORDER_ID + date;
        Long increment = redisService.increment(key, 1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

    /**
     * 删除下单商品的购物车信息
     */
    private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, UmsMember currentMember) {
        List<Long> ids = new ArrayList<>();
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            ids.add(cartPromotionItem.getId());
        }
        cartItemService.delete(currentMember.getId(), ids);
    }

    /**
     * 计算该订单赠送的成长值
     */
    private Integer calcGiftGrowth(List<OmsOrderItem> orderItemList) {
        Integer sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum = sum + orderItem.getGiftGrowth() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 计算该订单赠送的积分
     */
    private Integer calcGifIntegration(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum += orderItem.getGiftIntegration() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 将优惠券信息更改为指定状态
     *
     * @param couponId  优惠券id
     * @param memberId  会员id
     * @param useStatus 0->未使用；1->已使用
     */
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) return;
        //查询第一张优惠券
        SmsCouponHistoryExample example = new SmsCouponHistoryExample();
        example.createCriteria().andMemberIdEqualTo(memberId)
                .andCouponIdEqualTo(couponId).andUseStatusEqualTo(useStatus == 0 ? 1 : 0);
        List<SmsCouponHistory> couponHistoryList = couponHistoryMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(couponHistoryList)) {
            SmsCouponHistory couponHistory = couponHistoryList.get(0);
            couponHistory.setUseTime(new Date());
            couponHistory.setUseStatus(useStatus);
            couponHistoryMapper.updateByPrimaryKeySelective(couponHistory);
        }
    }

    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            //原价-促销优惠-优惠券抵扣-积分抵扣
            BigDecimal realAmount = orderItem.getProductPrice()
                    .subtract(orderItem.getPromotionAmount())
                    .subtract(orderItem.getCouponAmount())
                    .subtract(orderItem.getIntegrationAmount());
            orderItem.setRealAmount(realAmount);
        }
    }

    /**
     * 获取订单促销信息
     */
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            sb.append(orderItem.getPromotionName());
            sb.append(",");
        }
        String result = sb.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 计算订单应付金额
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        //总金额+运费-促销优惠-优惠券优惠-积分抵扣
        BigDecimal payAmount = order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount());
        return payAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal integrationAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getIntegrationAmount() != null) {
                integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return integrationAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getCouponAmount() != null) {
                couponAmount = couponAmount.add(orderItem.getCouponAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return couponAmount;
    }

    /**
     * 计算订单活动优惠
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(orderItem.getPromotionAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return promotionAmount;
    }

    /**
     * 获取可用积分抵扣金额
     *
     * @param useIntegration 使用的积分数量
     * @param totalAmount    订单总金额
     * @param currentMember  使用的用户
     * @param hasCoupon      是否已经使用优惠券
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember currentMember, boolean hasCoupon) {
        BigDecimal zeroAmount = new BigDecimal(0);
        //判断用户是否有这么多积分
        if (useIntegration.compareTo(currentMember.getIntegration()) > 0) {
            return zeroAmount;
        }
        //根据积分使用规则判断是否可用
        //是否可与优惠券共用
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
        if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
            //不可与优惠券共用
            return zeroAmount;
        }
        //是否达到最低使用积分门槛
        if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
            return zeroAmount;
        }
        //是否超过订单抵用最高百分比
        BigDecimal integrationAmount = new BigDecimal(useIntegration).divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);
        BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder()).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        if (integrationAmount.compareTo(totalAmount.multiply(maxPercent)) > 0) {
            return zeroAmount;
        }
        return integrationAmount;
    }

    /**
     * 对优惠券优惠进行处理
     *
     * @param orderItemList       order_item列表
     * @param couponHistoryDetail 可用优惠券详情
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            //全场通用
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getUseType().equals(1)) {
            //指定分类
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (coupon.getUseType().equals(2)) {
            //指定商品
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }

    /**
     * 对每个下单商品进行优惠券金额分摊的计算
     *
     * @param orderItemList 可用优惠券的下单商品商品
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem : orderItemList) {
            //(商品价格/可用商品总价)*优惠券面额
            BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }

    /**
     * 获取与优惠券有关系的下单商品
     *
     * @param couponHistoryDetail 优惠券详情
     * @param orderItemList       下单商品
     * @param type                使用关系类型：0->相关分类；1->指定商品
     */
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIdList = new ArrayList<>();
            for (SmsCouponProductCategoryRelation productCategoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                categoryIdList.add(productCategoryRelation.getProductCategoryId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (categoryIdList.contains(orderItem.getProductCategoryId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        } else if (type == 1) {
            List<Long> productIdList = new ArrayList<>();
            for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                productIdList.add(productRelation.getProductId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (productIdList.contains(orderItem.getProductId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        }
        return result;
    }

    /**
     * 获取该用户可以使用的优惠券
     *
     * @param cartPromotionItemList 购物车优惠列表
     * @param couponId              使用优惠券id
     */
    private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        for (SmsCouponHistoryDetail couponHistoryDetail : couponHistoryDetailList) {
            if (couponHistoryDetail.getCoupon().getId().equals(couponId)) {
                return couponHistoryDetail;
            }
        }
        return null;
    }

    /**
     * 计算总金额
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return totalAmount;
    }

    /**
     * 锁定下单商品的所有库存
     */
    private void lockStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(cartPromotionItem.getProductSkuId());
            //库存区分有无规格
            if (skuStock != null) {
                skuStock.setLockStock(skuStock.getLockStock() + cartPromotionItem.getQuantity());
                skuStockMapper.updateByPrimaryKeySelective(skuStock);
            } else {
                PmsProduct pmsProduct = pmsProductMapper.selectByPrimaryKey(cartPromotionItem.getProductId());
                pmsProduct.setLockStock(pmsProduct.getLockStock() + cartPromotionItem.getQuantity());
                pmsProductMapper.updateByPrimaryKeySelective(pmsProduct);
            }
        }
    }

    /**
     * 判断下单商品是否都有库存
     */
    private boolean hasStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            if (cartPromotionItem.getRealStock() == null || cartPromotionItem.getRealStock() <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算购物车中商品的价格
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal(0));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            totalAmount = totalAmount.add(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
            promotionAmount = promotionAmount.add(cartPromotionItem.getReduceAmount().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
        return calcAmount;
    }

    /**
     * 获取微信支付JSAPI支付参数
     */
    private WxPayMpOrderResult getWxPayJsParams(String body, String orderSn, String openId, BigDecimal amount) throws WxPayException {

        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();

        request.setSignType(WxPayConstants.SignType.MD5);

        //商品描述
        request.setBody(body);

        //商家订单号
        request.setOutTradeNo(orderSn);

        //openId
        request.setOpenid(openId);

        //金额
        BigDecimal unit = new BigDecimal("100");
        Integer totalFee = Convert.toInt(amount.multiply(unit), 0);
        request.setTotalFee(totalFee);

        //ip
        request.setSpbillCreateIp(NetUtil.LOCAL_IP);

        //交易类型
        request.setTradeType(WxPayConstants.TradeType.JSAPI);

        WxPayMpOrderResult wxPayResult = this.wxService.createOrder(request);
        return wxPayResult;
    }

    /**
     * 订单是否已评价
     */
    private Boolean orderIsComment(Long orderId) {
        PmsCommentExample commentExample = new PmsCommentExample();
        commentExample.createCriteria().andOrderIdEqualTo(orderId);
        List<PmsComment> commentList = pmsCommentMapper.selectByExample(commentExample);
        if (CollectionUtils.isEmpty(commentList)) {
            return false;
        }
        return true;
    }

    /**
     * 订单商品是否可售后
     */
    private void orderProductIsAfterSale(List<OrderItemDetailResult> orderItemDetails) {
        //查询是否存在待处理或已完成或退货中的售后单
        List<Integer> checkStatus = new ArrayList();
        checkStatus.add(0);
        checkStatus.add(1);
        checkStatus.add(2);
        UmsMember currentMember = memberService.getCurrentMember();
        for (OrderItemDetailResult orderItemDetail :orderItemDetails) {
            OmsOrderReturnApplyExample example = new OmsOrderReturnApplyExample();
            example.createCriteria().andOrderIdEqualTo(orderItemDetail.getOrderId()).andProductIdEqualTo(orderItemDetail.getProductId())
                    .andMemberUsernameEqualTo(currentMember.getUsername()).andStatusIn(checkStatus);
            List<OmsOrderReturnApply> existRealApplys = returnApplyMapper.selectByExample(example);
            if(CollectionUtil.isEmpty(existRealApplys))
            {
                orderItemDetail.setCanAftersale(true);
            }
            else {
                orderItemDetail.setCanAftersale(false);
            }
        }
    }

    /**
     * 释放订单的商品锁定库存-区分有无规格
     */
    private void releaseProductStockLock(List<OmsOrderItem> allOrderItems) {
        //获取有规格的订单信息表
        List<OmsOrderItem> hasSkuOrderItems = new ArrayList<>();
        List<OmsOrderItem> noSkuOrderItems = new ArrayList<>();
        for (OmsOrderItem allOrderItem : allOrderItems) {
            if (allOrderItem.getProductSkuId() != null && allOrderItem.getProductSkuId() > 0) {
                hasSkuOrderItems.add(allOrderItem);
            } else {
                noSkuOrderItems.add(allOrderItem);
            }
        }

        //修改有规格订单商品库存锁定库存及真实库存
        if (CollectionUtil.isNotEmpty(hasSkuOrderItems)) {
            //解除有规格订单商品库存锁定
            portalOrderDao.releaseSkuStockLock(hasSkuOrderItems);
        }

        //修改无规格订单商品库存锁定库存及真实库存
        if (CollectionUtil.isNotEmpty(noSkuOrderItems)) {
            //解除无规格订单商品库存锁定
            portalOrderDao.releaseNoSkuStockLock(noSkuOrderItems);
        }
    }

    /**
     * 修改订单的商品的锁定库存及真实库存-区分有无规格
     */
    private Integer updateProductStock(List<OmsOrderItem> allOrderItems) {
        //获取有规格的订单信息表
        List<OmsOrderItem> hasSkuOrderItems = new ArrayList<>();
        List<OmsOrderItem> noSkuOrderItems = new ArrayList<>();
        for (OmsOrderItem allOrderItem : allOrderItems) {
            if (allOrderItem.getProductSkuId() != null && allOrderItem.getProductSkuId() > 0) {
                hasSkuOrderItems.add(allOrderItem);
            } else {
                noSkuOrderItems.add(allOrderItem);
            }
        }

        //修改有规格订单商品库存锁定库存及真实库存
        Integer skuResult = 0, noSkuResult = 0;
        if (CollectionUtil.isNotEmpty(hasSkuOrderItems)) {
            skuResult = portalOrderDao.updateSkuStock(hasSkuOrderItems);
        }

        //修改无规格订单商品库存锁定库存及真实库存
        if (CollectionUtil.isNotEmpty(noSkuOrderItems)) {
            noSkuResult = portalOrderDao.updateNoSkuStock(noSkuOrderItems);
        }

        return skuResult + noSkuResult;
    }


    /**
     * 验证订单商品是否为可购买状态（未被删除处于上架并审核通过）
     */
    private String orderProductIsCanBuy(List<OmsOrderItem> orderItemList) {
        List<Long> orderProductIds = new ArrayList<>();
        for (OmsOrderItem orderItem : orderItemList) {
            if(!orderProductIds.contains(orderItem.getProductId()))
            {
                orderProductIds.add(orderItem.getProductId());
            }
        }

        List<PmsProduct> pmsProducts = portalProductDao.canNotBuyProductList(orderProductIds);
        if (CollectionUtil.isEmpty(pmsProducts)) {
            return "";
        }

        //不能购买的商品
        String canNoBuyProduct = "";
        if (CollectionUtil.isNotEmpty(pmsProducts)) {
            PmsProduct product = pmsProducts.get(0);
            if(product.getDeleteStatus() == 0){
                canNoBuyProduct += product.getName() + "已被删除";
            }
            else{
                canNoBuyProduct += product.getName() + "已被下架";
            }
        }
        return canNoBuyProduct;
    }
}
