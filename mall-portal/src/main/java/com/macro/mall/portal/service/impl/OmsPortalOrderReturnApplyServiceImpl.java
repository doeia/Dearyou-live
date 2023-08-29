package com.macro.mall.portal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.OmsOrderMapper;
import com.macro.mall.mapper.OmsOrderReturnApplyMapper;
import com.macro.mall.mapper.OmsOrderReturnReasonMapper;
import com.macro.mall.mapper.UmsMemberStatisticsInfoMapper;
import com.macro.mall.model.*;
import com.macro.mall.portal.domain.*;
import com.macro.mall.portal.service.OmsPortalOrderReturnApplyService;
import com.macro.mall.portal.service.UmsMemberService;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单退货管理Service实现类
 * Created by macro on 2018/10/17.
 */
@Service
public class OmsPortalOrderReturnApplyServiceImpl implements OmsPortalOrderReturnApplyService {
    @Autowired
    private OmsOrderReturnApplyMapper returnApplyMapper;
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberStatisticsInfoMapper memberStatisticsInfoMapper;
    @Autowired
    private OmsOrderReturnReasonMapper returnReasonMapper;
    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Override
    public CommonResult create(OmsOrderReturnApplyParam returnApply) {
        //查询是否存在待处理或已完成或退货中的售后单
        List<Integer> checkStatus = new ArrayList();
        checkStatus.add(0);
        checkStatus.add(1);
        checkStatus.add(2);
        UmsMember currentMember = memberService.getCurrentMember();
        OmsOrderReturnApplyExample example = new OmsOrderReturnApplyExample();
        example.createCriteria().andOrderIdEqualTo(returnApply.getOrderId()).andProductIdEqualTo(returnApply.getProductId())
                .andMemberUsernameEqualTo(currentMember.getUsername()).andStatusIn(checkStatus);
        List<OmsOrderReturnApply> existRealApplys = returnApplyMapper.selectByExample(example);
        if (CollectionUtil.isNotEmpty(existRealApplys)) {
            return CommonResult.failed("该商品已申请售后");
        }

        //不存在创建新的售后单
        OmsOrder order = omsOrderMapper.selectByPrimaryKey(returnApply.getOrderId());
        OmsOrderReturnApply realApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApply, realApply);
        realApply.setReturnAmount(returnApply.getProductRealPrice());
        realApply.setMemberUsername(currentMember.getUsername());
        realApply.setMemberNickname(currentMember.getNickname());
        realApply.setOrderSn(order.getOrderSn());
        realApply.setReturnName(order.getReceiverName());
        realApply.setReturnPhone(order.getReceiverPhone());
        realApply.setCreateTime(new Date());
        realApply.setStatus(0);
        Integer result = returnApplyMapper.insert(realApply);
        if (result > 0) {

            //修改用户统计信息
            //不存在则新增
            UmsMemberStatisticsInfoExample examples = new UmsMemberStatisticsInfoExample();
            examples.createCriteria().andMemberIdEqualTo(currentMember.getId());
            List<UmsMemberStatisticsInfo> umsMemberStatisticsInfos = memberStatisticsInfoMapper.selectByExample(examples);
            if(umsMemberStatisticsInfos.isEmpty())
            {
                UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
                umsMemberStatisticsInfo.setMemberId(currentMember.getId());
                umsMemberStatisticsInfo.setConsumeAmount(new BigDecimal(0));
                umsMemberStatisticsInfo.setOrderCount(0);
                umsMemberStatisticsInfo.setCouponCount(0);
                umsMemberStatisticsInfo.setCommentCount(0);
                umsMemberStatisticsInfo.setReturnOrderCount(1);
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
                umsMemberStatisticsInfo.setReturnOrderCount(umsMemberStatisticsInfo.getReturnOrderCount() + 1);
                memberStatisticsInfoMapper.updateByPrimaryKeySelective(umsMemberStatisticsInfo);
            }

            return CommonResult.success(result);
        }
        return CommonResult.failed();
    }

    @Override
    public CommonPage<OrderReturnApplyResult> list(OmsOrderReturnApplyListParam applyListParam) {
        //查询列表
        PageHelper.startPage(applyListParam.getPage().getPageNum(), applyListParam.getPage().getPageSize());
        OmsOrderReturnApplyExample example = new OmsOrderReturnApplyExample();
        OmsOrderReturnApplyExample.Criteria criteria = example.createCriteria().andMemberUsernameEqualTo(memberService.getCurrentMember().getUsername());

        if (applyListParam.getStatus() != null && applyListParam.getStatus() > -1) {
            criteria.andStatusEqualTo(applyListParam.getStatus());
        }

        //转换实体类
        List<OrderReturnApplyResult> orderReturnApplyList = new ArrayList();
        CommonPage<OmsOrderReturnApply> orderReturnApplyPageList = CommonPage.restPage(returnApplyMapper.selectByExample(example));
        if (orderReturnApplyPageList != null && CollectionUtil.isNotEmpty(orderReturnApplyPageList.getList())) {
            for (var returnApply : orderReturnApplyPageList.getList()) {
                OrderReturnApplyResult returnApplyResult = new OrderReturnApplyResult();
                BeanUtils.copyProperties(returnApply, returnApplyResult);
                String statusDesc = getStatusDesc(returnApplyResult.getStatus());
                returnApplyResult.setStatusDesc(statusDesc);
                orderReturnApplyList.add(returnApplyResult);
            }
        }

        var orderReturnApplyNewPageList = CommonPage.restPage(orderReturnApplyList);
        orderReturnApplyNewPageList.setTotal(orderReturnApplyPageList.getTotal());
        orderReturnApplyNewPageList.setTotalPage(orderReturnApplyPageList.getTotalPage());
        return orderReturnApplyNewPageList;
    }

    /**
     * 获取售后订单详情
     */
    @Override
    public OrderReturnApplyResult detail(Long id) {
        OrderReturnApplyResult orderReturnApply = new OrderReturnApplyResult();
        OmsOrderReturnApply omsOrderReturnApply =  returnApplyMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(omsOrderReturnApply,orderReturnApply);
        String statusDesc = getStatusDesc(orderReturnApply.getStatus());
        orderReturnApply.setStatusDesc(statusDesc);
        return orderReturnApply;
    }

    /**
     * 获取售后原因列表
     */
    public List<OmsOrderReturnReason> returnReasonList() {
        OmsOrderReturnReasonExample example = new OmsOrderReturnReasonExample();
        example.createCriteria().andStatusEqualTo(1);
        return returnReasonMapper.selectByExample(example);
    }

    /**
     * 获取售后原因状态描述
     */
    private String getStatusDesc(Integer status){
        String statusDesc;
        switch (status) {
            case 0:
                statusDesc = "待处理";
                break;
            case 1:
                statusDesc = "退货中";
                break;
            case 2:
                statusDesc = "已完成";
                break;
            case 3:
                statusDesc = "已拒绝";
                break;
            default:
                statusDesc = "未知";
                break;
        }
        return statusDesc;
    }
}
