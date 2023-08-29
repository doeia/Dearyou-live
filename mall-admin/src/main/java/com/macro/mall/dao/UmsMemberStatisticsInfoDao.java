package com.macro.mall.dao;

import com.macro.mall.dto.UmsMemberStatisticsInfoNumberResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoSexResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoTimeResult;
import com.macro.mall.model.UmsMemberStatisticsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsMemberStatisticsInfoDao {

    /**
     * 获取会员信息
     * @param memberId
     * @return
     */
    UmsMemberStatisticsInfo getStatInfo(@Param("memberId") Long memberId);

    /**
     * 获取男女统计信息
     * @return
     */
    UmsMemberStatisticsInfoSexResult getStatInfoOfSex();

    /**
     * 获取按月统计信息
     * @param mode
     * @param date
     * @return
     */
    List<UmsMemberStatisticsInfoTimeResult> getStatInfoOfTime(@Param("mode")Integer mode, @Param("date") String date);

    /**
     * 获取会员数量统计信息
     * @return
     */
    UmsMemberStatisticsInfoNumberResult getStatInfoOfNumber();
}
