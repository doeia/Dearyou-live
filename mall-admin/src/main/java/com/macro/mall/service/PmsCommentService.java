package com.macro.mall.service;

import com.macro.mall.dto.PmsCommentQueryParam;
import com.macro.mall.model.PmsComment;

import java.util.List;

/**
 * 商品评价表
 *
 * @author taobao
 */
public interface PmsCommentService {

    /**
     * 列出列表
     * @param queryParam
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<PmsComment> list(PmsCommentQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    PmsComment getItem(Long id);

    /**
     * 创建
     * @param comment
     * @return
     */
    int create(PmsComment comment);

    /**
     * 更新
     * @param id
     * @param comment
     * @return
     */
    int update(Long id, PmsComment comment);

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
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(Long id, Integer status);
}