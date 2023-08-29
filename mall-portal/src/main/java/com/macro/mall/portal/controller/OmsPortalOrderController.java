package com.macro.mall.portal.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.CmsAnnouncementMapper;
import com.macro.mall.mapper.UmsMemberLoginLogMapper;
import com.macro.mall.model.CmsAnnouncement;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.UmsMember;
import com.macro.mall.model.UmsMemberLoginLog;
import com.macro.mall.portal.domain.*;
import com.macro.mall.portal.service.OmsPortalOrderService;
import com.macro.mall.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 订单管理Controller
 * Created by macro on 2018/8/30.
 */
@Controller
@Api(tags = "OmsPortalOrderController", description = "订单管理")
@RequestMapping("/order")
public class OmsPortalOrderController {
    @Autowired
    private OmsPortalOrderService portalOrderService;
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private WxPayService wxService;
    @Autowired
    private CmsAnnouncementMapper cmsAnnouncementMapper;


//    @ApiOperation("根据购物车信息生成确认单信息")
//    @RequestMapping(value = "/generateConfirmOrder",method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult<ConfirmOrderResult> generateConfirmOrder(){
//        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder();
//        return CommonResult.success(confirmOrderResult);
//    }

    @ApiOperation("根据购物车信息生成确认单信息")
    @RequestMapping(value = "/generateConfirmOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ConfirmOrderResult> generateConfirmOrder(@RequestBody ConfirmOrderParam confirmOrderParam) {
        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder(confirmOrderParam);
        return CommonResult.success(confirmOrderResult);
    }

    @ApiOperation("根据购物车信息生成订单")
    @RequestMapping(value = "/generateOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<WxPayMpOrderResult> generateOrder(@RequestBody OrderParam orderParam) throws WxPayException {
        return portalOrderService.generateOrder(orderParam);
    }

    @ApiOperation("重新支付订单")
    @RequestMapping(value = "/repayOrder/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<WxPayMpOrderResult> repayOrder(@PathVariable Long id) throws WxPayException {
        return portalOrderService.repayOrder(id);
    }

    @ApiOperation("支付成功的回调")
    @RequestMapping(value = "/paySuccess", method = RequestMethod.POST)
    @ResponseBody
    public Object paySuccess(@RequestParam Long orderId) {
        return portalOrderService.paySuccess(orderId);
    }

    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/wxNotify")
    public String wxNotify(@RequestBody String xmlData) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = this.wxService.parseOrderNotifyResult(xmlData);
        OmsOrder order = portalOrderService.getOrderBySn(notifyResult.getOutTradeNo());
        CommonResult result = portalOrderService.onlinePaySuccess(order.getId(), notifyResult.getTransactionId());
        if (result.getCode() == 200) {
            return WxPayNotifyResponse.success("成功");
        } else {
            return WxPayNotifyResponse.fail("失败");
        }
    }

    @ApiOperation("获取当前会员的订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<OrderResult>> list(@RequestBody OmsOrderParam orderParam) {
        List<OrderResult> omsOrderList = portalOrderService.getOrderList(orderParam);
        return CommonResult.success(CommonPage.restPage(omsOrderList));
    }

    @ApiOperation("获取订单详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<OrderDetailResult> detail(@PathVariable Long id) {
        OrderDetailResult orderDetail = portalOrderService.getOrderDetail(id);
        return CommonResult.success(orderDetail);
    }

    @ApiOperation("自动取消超时订单")
    @RequestMapping(value = "/cancelTimeOutOrder", method = RequestMethod.POST)
    @ResponseBody
    public Object cancelTimeOutOrder() {
        return portalOrderService.cancelTimeOutOrder();
    }

    @ApiOperation("取消单个超时订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult cancelOrder(Long orderId) {
        portalOrderService.sendDelayMessageCancelOrder(orderId);
        return CommonResult.success(null);
    }

    @ApiOperation("取消单个订单")
    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult cancel(@PathVariable Long id) {
        return portalOrderService.cancelOrder(id);
    }

    @ApiOperation("确认收货单个订单")
    @RequestMapping(value = "/receive/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult receive(@PathVariable Long id) {
        return portalOrderService.receiveOrder(id);
    }

    @ApiOperation("评论单个订单")
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult comment(@RequestBody CommentOrderParam commentOrderParam) {
        return portalOrderService.commentOrder(commentOrderParam);
    }
}
