package com.macro.mall.service;

import com.macro.mall.dto.OmsOrderStatisticsInfoDateResult;
import com.macro.mall.dto.OmsOrderStatisticsInfoResult;

import java.util.List;

public interface OmsOrderStatisticsInfoService {
    OmsOrderStatisticsInfoResult getStatInfo();
    List<OmsOrderStatisticsInfoDateResult> getStatInfoOfDate(String startDateStr, String endDateStr);
}
