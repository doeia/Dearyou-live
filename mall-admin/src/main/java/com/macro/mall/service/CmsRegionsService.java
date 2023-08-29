package com.macro.mall.service;

import com.macro.mall.dto.CmsRegionsNode;
import com.macro.mall.dto.CmsRegionsParam;
import com.macro.mall.model.CmsRegions;

import java.util.List;

/**
 * 区域表
 *
 * @author taobao
 */
public interface CmsRegionsService {

    /**
     * 列出列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<CmsRegions> list(Integer pageSize, Integer pageNum);

    /**
     * 获取树型列表
     * @return
     */
    List<CmsRegionsNode> treeList();

    /**
     * 获取详情
     * @param id
     * @return
     */
    CmsRegions getItem(Long id);

    /**
     * 创建
     * @param param
     * @param operatorId
     * @param operator
     * @return
     */
    int create(CmsRegionsParam param, Long operatorId, String operator);

    /**
     * 更新
     * @param id
     * @param param
     * @param operatorId
     * @param operator
     * @return
     */
    int update(Long id, CmsRegionsParam param, Long operatorId, String operator);

    /**
     * 丢弃
     * @param id
     * @return
     */
    int drop(Long id, Long operatorId, String operator);

}