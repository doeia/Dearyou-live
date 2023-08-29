package com.macro.mall.service;

import com.macro.mall.dto.PmsCommentReplayQueryParam;
import com.macro.mall.model.PmsCommentReplay;

import java.util.List;

/**
 * 产品评价回复表
 *
 * @author taobao
 */
public interface PmsCommentReplayService {

    /**
     * 列出列表
     * @param queryParam
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<PmsCommentReplay> list(PmsCommentReplayQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    PmsCommentReplay getItem(Long id);

    /**
     * 创建
     * @param commentReplay
     * @return
     */
    int create(PmsCommentReplay commentReplay);

    /**
     * 更新
     * @param id
     * @param commentReplay
     * @return
     */
    int update(Long id, PmsCommentReplay commentReplay);

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