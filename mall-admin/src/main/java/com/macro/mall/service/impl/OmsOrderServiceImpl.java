package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.dao.OmsOrderDao;
import com.macro.mall.dao.OmsOrderOperateHistoryDao;
import com.macro.mall.dto.*;
import com.macro.mall.mapper.OmsOrderMapper;
import com.macro.mall.mapper.OmsOrderOperateHistoryMapper;
import com.macro.mall.mapper.OmsShipCompaniesMapper;
import com.macro.mall.model.*;
import com.macro.mall.service.ExpressService;
import com.macro.mall.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理Service实现类
 * Created by macro on 2018/10/11.
 */
@Service
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsOrderDao orderDao;
    @Autowired
    private OmsOrderOperateHistoryDao orderOperateHistoryDao;
    @Autowired
    private OmsOrderOperateHistoryMapper orderOperateHistoryMapper;

    @Autowired
    private OmsShipCompaniesMapper shipCompaniesMapper;
    @Autowired
    private ExpressService expressService;

    @Override
    public List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return orderDao.getList(queryParam);
    }

    @Override
    public int delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
        //批量发货
        int count = orderDao.delivery(deliveryParamList);
        List<Long> ids = deliveryParamList.stream().map(x -> {return x.getOrderId();}).collect(Collectors.toList());
        recordHistory(ids,2,"完成发货");
        return count;
    }

    @Override
    public int close(List<Long> ids, String note) {
        OmsOrder record = new OmsOrder();
        record.setStatus(4);
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        int count = orderMapper.updateByExampleSelective(record, example);
        recordHistory(ids,4,"订单关闭:" + note);
        return count;
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrder record = new OmsOrder();
        record.setDeleteStatus(1);
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        return orderMapper.updateByExampleSelective(record, example);
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        return orderDao.getDetail(id);
    }

    @Override
    public int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        recordHistory(receiverInfoParam.getOrderId(),receiverInfoParam.getStatus(),"修改收货人信息");
        return count;
    }

    @Override
    public int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        recordHistory(moneyInfoParam.getOrderId(),moneyInfoParam.getStatus(),"修改费用信息");
        return count;
    }

    @Override
    public int updateNote(Long id, String note, Integer status) {
        OmsOrder order = new OmsOrder();
        order.setId(id);
        order.setNote(note);
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        recordHistory(id,status,"修改备注信息：" + note);
        return count;
    }

    @Override
    public ExpressResult getLogistics(Long id) {
        OmsOrder order = orderMapper.selectByPrimaryKey(id);
        if(order==null || StringUtils.isEmpty(order.getDeliveryCompany()) || StringUtils.isEmpty(order.getDeliverySn())){
            return null;
        }
        OmsShipCompaniesExample example = new OmsShipCompaniesExample();
        example.createCriteria().andNameEqualTo(order.getDeliveryCompany());
        List<OmsShipCompanies> shipCompanies = shipCompaniesMapper.selectByExample(example);
        if(shipCompanies.size()==0){
            return null;
        }
        String com = shipCompanies.get(0).getCode();
        if(StringUtils.isEmpty(com)){
            return null;
        }

        ExpressResult result = expressService.queryLogistics(com,order.getDeliverySn());
        if(result==null){
            result = new ExpressResult();
            result.setState(0);
            result.setData(new ArrayList<>());
        }
        return result;
    }

    private void recordHistory(Long id, Integer orderStatus, String note){
        try {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(id);
            history.setCreateTime(new Date());
            history.setOperateMan("后台管理员");
            history.setOrderStatus(orderStatus);
            history.setNote(note);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal != null && principal instanceof String){
                history.setOperateMan((String) principal);
            }else {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails != null) {
                    history.setOperateMan(userDetails.getUsername());
                }
            }
            orderOperateHistoryMapper.insert(history);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void recordHistory(List<Long> ids, Integer orderStatus, String note){
        try {
            final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId -> {
                OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                history.setOrderId(orderId);
                history.setCreateTime(new Date());
                history.setOperateMan("后台管理员");
                history.setOrderStatus(orderStatus);
                history.setNote(note);

                if (principal != null && principal instanceof String) {
                    history.setOperateMan((String) principal);
                } else {
                    UserDetails userDetails = (UserDetails) principal;
                    if (userDetails != null) {
                        history.setOperateMan(userDetails.getUsername());
                    }
                }
                return history;
            }).collect(Collectors.toList());
            orderOperateHistoryDao.insertList(historyList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
