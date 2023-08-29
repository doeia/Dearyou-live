package com.macro.mall.portal.service;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.OmsOrderReturnApply;
import com.macro.mall.model.OmsOrderReturnReason;
import com.macro.mall.portal.domain.OmsOrderReturnApplyListParam;
import com.macro.mall.portal.domain.OmsOrderReturnApplyParam;
import com.macro.mall.portal.domain.OrderReturnApplyResult;

import java.util.List;

/**
 * 订单退货管理Service
 * Created by macro on 2018/10/17.
 */
public interface OmsPortalOrderReturnApplyService {
    /**
     * 提交申请
     */
    CommonResult create(OmsOrderReturnApplyParam returnApply);

    /**
     * 获取售后订单列表
     */
    CommonPage<OrderReturnApplyResult> list(OmsOrderReturnApplyListParam applyListParam);

    /**
     * 获取售后订单详情
     */
    OrderReturnApplyResult detail(Long id);

    /**
     * 获取售后原因列表
     */
    List<OmsOrderReturnReason> returnReasonList();
}
