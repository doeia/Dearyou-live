package com.macro.mall.service;

import com.macro.mall.model.PmsFeightTemplate;

import java.util.List;

/**
 * 运费模版
 *
 * @author taobao
 */
public interface PmsFeightTemplateService {

    /**
     * 列出列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<PmsFeightTemplate> list(Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    PmsFeightTemplate getItem(Long id);

    /**
     * 创建
     * @param feightTemplate
     * @return
     */
    int create(PmsFeightTemplate feightTemplate);

    /**
     * 更新
     * @param id
     * @param feightTemplate
     * @return
     */
    int update(Long id, PmsFeightTemplate feightTemplate);

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