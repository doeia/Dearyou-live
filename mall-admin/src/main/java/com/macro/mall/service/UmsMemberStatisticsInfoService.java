package com.macro.mall.service;

import com.macro.mall.dto.UmsMemberStatisticsInfoNumberResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoSexResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoTimeResult;
import com.macro.mall.model.UmsMemberStatisticsInfo;

import java.util.List;

/**
 * 会员统计信息
 *
 * @author taobao
 * @date Sun Nov 17 11:02:46 CST 2019
 */
public interface UmsMemberStatisticsInfoService {

    /**
     * 列出列表
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<UmsMemberStatisticsInfo> list(Integer pageSize, Integer pageNum);

    /**
     * 获取详情
     * @param id
     * @return
     */
    UmsMemberStatisticsInfo getItem(Long id);

    /**
     * 创建
     * @param memberStatisticsInfo
     * @return
     */
    int create(UmsMemberStatisticsInfo memberStatisticsInfo);

    /**
     * 更新
     * @param id
     * @param memberStatisticsInfo
     * @return
     */
    int update(Long id, UmsMemberStatisticsInfo memberStatisticsInfo);

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
     * 获取详情
     * @param memberId
     * @return
     */
    UmsMemberStatisticsInfo getStatInfo(Long memberId);

    /**
     * 获取会员男女比例信息
     * @return
     */
    UmsMemberStatisticsInfoSexResult getStatInfoOfSex();

    /**
     * 按时间获取信息
     * @param mode
     * @param date
     * @return
     */
    List<UmsMemberStatisticsInfoTimeResult> getStatInfoOfTime(Integer mode, String date);

    /**
     * 获取会员数量统计信息
     * @return
     */
    UmsMemberStatisticsInfoNumberResult getStatInfoOfNumber();
}