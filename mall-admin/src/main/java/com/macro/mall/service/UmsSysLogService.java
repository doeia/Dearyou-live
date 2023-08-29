package com.macro.mall.service;

import com.macro.mall.model.UmsSysLog;

import java.util.List;

/**
 * 日志管理Service
 */
public interface UmsSysLogService {
    /**
     * 列出日志列表
     * @param admin
     * @param deleted
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<UmsSysLog> list(String admin, Integer deleted, Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    UmsSysLog getItem(Long id);

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
     * 丢弃
     * @param id
     * @return
     */
    int drop(Long id);

    /**
     * 批量丢弃
     * @param ids
     * @return
     */
    int drop(List<Long> ids);

    /**
     * 还原
     * @param id
     * @return
     */
    int recover(Long id);

    /**
     * 批量还原
     * @param ids
     * @return
     */
    int recover(List<Long> ids);
}
