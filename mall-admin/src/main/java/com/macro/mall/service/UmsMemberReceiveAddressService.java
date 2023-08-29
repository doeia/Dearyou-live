package com.macro.mall.service;

import com.macro.mall.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * 收货地址Service
 */
public interface UmsMemberReceiveAddressService {
    List<UmsMemberReceiveAddress> list(Integer pageSize, Integer pageNum);
    UmsMemberReceiveAddress getItem(Long id);
}
