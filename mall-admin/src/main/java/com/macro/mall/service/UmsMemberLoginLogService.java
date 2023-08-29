package com.macro.mall.service;

import com.macro.mall.model.UmsMemberLoginLog;

import java.util.List;

/**
 * 会员登录记录
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
public interface UmsMemberLoginLogService {

    /**
     * 列出列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<UmsMemberLoginLog> list(Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    UmsMemberLoginLog getItem(Long id);

    /**
     * 创建
     * @param memberLoginLog
     * @return
     */
    int create(UmsMemberLoginLog memberLoginLog);

    /**
     * 更新
     * @param id
     * @param memberLoginLog
     * @return
     */
    int update(Long id, UmsMemberLoginLog memberLoginLog);

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