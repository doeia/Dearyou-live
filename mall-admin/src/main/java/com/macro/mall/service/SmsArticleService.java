package com.macro.mall.service;

import com.macro.mall.model.SmsArticle;

import java.util.List;

/**
 * 文章表
 *
 * @author taobao
 * @date Sun Nov 17 16:44:14 CST 2019
 */
public interface SmsArticleService {

    /**
     * 列出列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<SmsArticle> list(Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    SmsArticle getItem(Long id);

    /**
     * 创建
     * @param article
     * @return
     */
    int create(SmsArticle article);

    /**
     * 更新
     * @param id
     * @param article
     * @return
     */
    int update(Long id, SmsArticle article);

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