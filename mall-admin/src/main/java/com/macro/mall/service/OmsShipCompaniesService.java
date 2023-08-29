package com.macro.mall.service;

import com.macro.mall.model.OmsShipCompanies;

import java.util.List;

/**
 * 配送公司表
 *
 * @author taobao
 */
public interface OmsShipCompaniesService {

    /**
     * 列出列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<OmsShipCompanies> list(Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    OmsShipCompanies getItem(Long id);

    /**
     * 创建
     * @param shipCompanies
     * @return
     */
    int create(OmsShipCompanies shipCompanies);

    /**
     * 更新
     * @param id
     * @param shipCompanies
     * @return
     */
    int update(Long id, OmsShipCompanies shipCompanies);

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