package com.macro.mall.service;

import com.macro.mall.model.UmsAdminLoginLog;

import java.util.List;

/**
 * 后台用户登录日志表
 *
 * @author taobao
 * @date Sun Nov 17 16:44:14 CST 2019
 */
public interface UmsAdminLoginLogService {

    /**
     * 列出列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<UmsAdminLoginLog> list(Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    UmsAdminLoginLog getItem(Long id);

    /**
     * 创建
     * @param adminLoginLog
     * @return
     */
    int create(UmsAdminLoginLog adminLoginLog);

    /**
     * 更新
     * @param id
     * @param adminLoginLog
     * @return
     */
    int update(Long id, UmsAdminLoginLog adminLoginLog);

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