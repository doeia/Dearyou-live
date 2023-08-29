package com.macro.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.SmsCouponHistoryMapper;
import com.macro.mall.mapper.SmsCouponMapper;
import com.macro.mall.mapper.UmsMemberStatisticsInfoMapper;
import com.macro.mall.model.*;
import com.macro.mall.portal.dao.SmsCouponHistoryDao;
import com.macro.mall.portal.domain.CartPromotionItem;
import com.macro.mall.portal.domain.MemberCouponListResult;
import com.macro.mall.portal.domain.SmsCouponHistoryDetail;
import com.macro.mall.portal.service.UmsMemberCouponService;
import com.macro.mall.portal.service.UmsMemberService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员优惠券管理Service实现类
 * Created by macro on 2018/8/29.
 */
@Service
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberStatisticsInfoMapper memberStatisticsInfoMapper;
    @Autowired
    private SmsCouponMapper couponMapper;
    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;
    @Autowired
    private SmsCouponHistoryDao couponHistoryDao;
    @Override
    public CommonResult add(Long couponId) {
        UmsMember currentMember = memberService.getCurrentMember();
        //获取优惠券信息，判断数量
        SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
        if(coupon==null){
            return CommonResult.failed("优惠券不存在");
        }
        if(coupon.getCount()<=0){
            return CommonResult.failed("优惠券已经领完了");
        }
        Date now = new Date();
        if(now.before(coupon.getEnableTime())){
            return CommonResult.failed("优惠券还没到领取时间");
        }
        //判断用户领取的优惠券数量是否超过限制
        SmsCouponHistoryExample couponHistoryExample = new SmsCouponHistoryExample();
        couponHistoryExample.createCriteria().andCouponIdEqualTo(couponId).andMemberIdEqualTo(currentMember.getId());
        long count = couponHistoryMapper.countByExample(couponHistoryExample);
        if(count>=coupon.getPerLimit()){
            return CommonResult.failed("您已经领取过该优惠券");
        }
        //生成领取优惠券历史
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setCouponId(couponId);
        couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
        couponHistory.setCreateTime(now);
        couponHistory.setMemberId(currentMember.getId());
        couponHistory.setMemberNickname(currentMember.getNickname());
        //主动领取
        couponHistory.setGetType(1);
        //未使用
        couponHistory.setUseStatus(0);
        couponHistoryMapper.insert(couponHistory);
        //修改优惠券表的数量、领取数量
        coupon.setCount(coupon.getCount()-1);
        coupon.setReceiveCount(coupon.getReceiveCount()==null?1:coupon.getReceiveCount()+1);
        couponMapper.updateByPrimaryKey(coupon);

        //修改用户统计信息
        //不存在则新增
        UmsMemberStatisticsInfoExample example = new UmsMemberStatisticsInfoExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId());
        List<UmsMemberStatisticsInfo> umsMemberStatisticsInfos = memberStatisticsInfoMapper.selectByExample(example);
        if(umsMemberStatisticsInfos.isEmpty())
        {
            UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
            umsMemberStatisticsInfo.setMemberId(currentMember.getId());
            umsMemberStatisticsInfo.setConsumeAmount(new BigDecimal(0));
            umsMemberStatisticsInfo.setOrderCount(0);
            umsMemberStatisticsInfo.setCouponCount(1);
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
            memberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
        }else
        {
            UmsMemberStatisticsInfo umsMemberStatisticsInfo = umsMemberStatisticsInfos.get(0);
            umsMemberStatisticsInfo.setCouponCount(umsMemberStatisticsInfo.getCouponCount() + 1);
            memberStatisticsInfoMapper.updateByPrimaryKeySelective(umsMemberStatisticsInfo);
        }

        return CommonResult.success(null,"领取成功");
    }

    /**
     * 16位优惠码生成：时间戳后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        Long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = currentTimeMillis.toString();
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length()-4));
        }
        return sb.toString();
    }

    @Override
    public CommonPage<MemberCouponListResult> list(String couponCode, Integer useStatus, int pageSize, int pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);

        UmsMember currentMember = memberService.getCurrentMember();
        SmsCouponHistoryExample couponHistoryExample=new SmsCouponHistoryExample();
        SmsCouponHistoryExample.Criteria criteria = couponHistoryExample.createCriteria();
        criteria.andMemberIdEqualTo(currentMember.getId());
        if(useStatus!=null){
            criteria.andUseStatusEqualTo(useStatus);
        }
        if(!couponCode.isEmpty())
        {
            criteria.andCouponCodeLike("%" + couponCode + "%");
        }

        var couponList = couponHistoryMapper.selectByExample(couponHistoryExample);

        var pageList = CommonPage.restPage(couponList);

        List<MemberCouponListResult> list = new ArrayList<>();

        for (var info : couponList)
        {
            var coupon = couponMapper.selectByPrimaryKey(info.getCouponId());
            if(coupon != null)
            {
                MemberCouponListResult memberCouponListResult = new MemberCouponListResult();
                memberCouponListResult.setCouponId(coupon.getId() != null ? coupon.getId() : 0);
                memberCouponListResult.setAmount(coupon.getAmount() != null ? coupon.getAmount() : new BigDecimal(0));
                memberCouponListResult.setName(coupon.getName() != null ? coupon.getName() : "");
                memberCouponListResult.setNote(coupon.getNote() != null ? coupon.getNote() : "");
                SimpleDateFormat sdf=new SimpleDateFormat("MM.dd");
                memberCouponListResult.setStartTime(coupon.getStartTime() != null ? sdf.format(coupon.getStartTime()) : "00.00");
                memberCouponListResult.setEndTime(coupon.getEndTime() != null ? sdf.format(coupon.getEndTime()) : "00.00");

                list.add(memberCouponListResult);
            }

        }

        var newPageList = CommonPage.restPage(list);
        newPageList.setTotalPage(pageList.getTotalPage());
        newPageList.setTotal(pageList.getTotal());

        return newPageList;
    }

    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type) {
        UmsMember currentMember = memberService.getCurrentMember();
        Date now = new Date();
        //获取该用户所有优惠券
        List<SmsCouponHistoryDetail> allList = couponHistoryDao.getDetailList(currentMember.getId());
        //根据优惠券使用类型来判断优惠券是否可用
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if(useType.equals(0)){
                //0->全场通用
                //判断是否满足优惠起点
                //计算购物车商品的总价
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if(now.before(endTime)&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }else if(useType.equals(1)){
                //1->指定分类
                //计算指定分类商品的总价
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelation categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }
                BigDecimal totalAmount = calcTotalAmountByproductCategoryId(cartItemList,productCategoryIds);
                if(now.before(endTime)&&totalAmount.intValue()>0&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }else if(useType.equals(2)){
                //2->指定商品
                //计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList,productIds);
                if(now.before(endTime)&&totalAmount.intValue()>0&&totalAmount.subtract(minPoint).intValue()>=0){
                    enableList.add(couponHistoryDetail);
                }else{
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        if(type.equals(1)){
            return enableList;
        }else{
            return disableList;
        }
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
            total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByproductCategoryId(List<CartPromotionItem> cartItemList,List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if(productCategoryIds.contains(item.getProductCategoryId())){
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList,List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if(productIds.contains(item.getProductId())){
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total=total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

}
