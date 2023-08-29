package com.macro.mall.service;

import com.macro.mall.model.UmsMemberLevel;

import java.util.List;

/**
 * 会员等级
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
public interface UmsMemberLevelService {

    /**
     * 列出列表
     * @return
     */
    List<UmsMemberLevel> list();

    /**
     * 获取详情
     * @param id
     * @return
     */
    UmsMemberLevel getItem(Long id);

    /**
     * 创建
     * @param memberLevel
     * @return
     */
    int create(UmsMemberLevel memberLevel);

    /**
     * 更新
     * @param id
     * @param memberLevel
     * @return
     */
    int update(Long id, UmsMemberLevel memberLevel);

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