package com.macro.mall.service;

import com.macro.mall.model.CmsAnnouncement;

import java.util.List;

/**
 * 公告管理service
 */
public interface CmsAnnouncementService {
    /**
     * 列出公告列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<CmsAnnouncement> list(Integer pageSize, Integer pageNum);

    /**
     * 获取公告详情
     * @param id
     * @return
     */
    CmsAnnouncement getItem(Long id);

    /**
     * 创建公告
     * @param announcement
     * @return
     */
    int create(CmsAnnouncement announcement);

    /**
     * 更新公告
     * @param id
     * @param announcement
     * @return
     */
    int update(Long id, CmsAnnouncement announcement);

    /**
     * 删除公告
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
