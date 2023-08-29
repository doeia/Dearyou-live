package com.macro.mall.service;

import com.macro.mall.model.PmsBrandCategory;

import java.util.List;

/**
 * 品牌分类
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
public interface PmsBrandCategoryService {

    /**
     * 列出列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<PmsBrandCategory> list(Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    PmsBrandCategory getItem(Long id);

    /**
     * 创建
     * @param brandCategory
     * @return
     */
    int create(PmsBrandCategory brandCategory);

    /**
     * 更新
     * @param id
     * @param brandCategory
     * @return
     */
    int update(Long id, PmsBrandCategory brandCategory);

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

    /**
     * 列出所有
     * @return
     */
    List<PmsBrandCategory> list();
}