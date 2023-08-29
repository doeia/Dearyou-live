package com.macro.mall.dao;

import com.macro.mall.dto.OmsOrderStatisticsInfoDateResult;
import com.macro.mall.dto.OmsOrderStatisticsInfoResult;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OmsOrderStatisticsInfoDao {
    OmsOrderStatisticsInfoResult getStatInfo();
    List<OmsOrderStatisticsInfoDateResult> getStatInfoOfDate(@Param("startDate")Date startDate, @Param("endDate") Date endDate);
}
