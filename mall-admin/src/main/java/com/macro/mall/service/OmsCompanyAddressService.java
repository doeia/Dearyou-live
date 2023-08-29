package com.macro.mall.service;

import com.macro.mall.model.OmsCompanyAddress;

import java.util.List;

/**
 * 收货地址管Service
 * Created by macro on 2018/10/18.
 */
public interface OmsCompanyAddressService {
    /**
     * 获取全部收货地址
     */
    List<OmsCompanyAddress> list();
	
	/**
     * 获取详情
     * @param id
     * @return
     */
    OmsCompanyAddress getItem(Long id);

    /**
     * 创建
     * @param companyAddress
     * @return
     */
    int create(OmsCompanyAddress companyAddress);

    /**
     * 更新
     * @param id
     * @param companyAddress
     * @return
     */
    int update(Long id, OmsCompanyAddress companyAddress);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int delete(List<Long> ids);
}
