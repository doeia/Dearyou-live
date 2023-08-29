package com.macro.mall.service;

import com.macro.mall.model.UmsMemberKeywords;

import java.util.List;

/**
 * 会员与关键词表（用户搜索的关键词）
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
public interface UmsMemberKeywordsService {

    /**
     * 列出列表
     * @param memberId
     * @param keywords
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<UmsMemberKeywords> list(Long memberId, String keywords, Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    UmsMemberKeywords getItem(Long id);

    /**
     * 创建
     * @param memberKeywords
     * @return
     */
    int create(UmsMemberKeywords memberKeywords);

    /**
     * 更新
     * @param id
     * @param memberKeywords
     * @return
     */
    int update(Long id, UmsMemberKeywords memberKeywords);

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