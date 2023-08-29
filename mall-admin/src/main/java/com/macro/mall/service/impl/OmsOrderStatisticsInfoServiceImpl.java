package com.macro.mall.service.impl;

import cn.hutool.core.date.DateUtil;
import com.macro.mall.dao.OmsOrderStatisticsInfoDao;
import com.macro.mall.dto.OmsOrderStatisticsInfoDateResult;
import com.macro.mall.dto.OmsOrderStatisticsInfoResult;
import com.macro.mall.service.OmsOrderStatisticsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OmsOrderStatisticsInfoServiceImpl implements OmsOrderStatisticsInfoService {
    @Autowired
    private OmsOrderStatisticsInfoDao orderStatisticsInfoDao;

    @Override
    public OmsOrderStatisticsInfoResult getStatInfo() {
        return orderStatisticsInfoDao.getStatInfo();
    }

    @Override
    public List<OmsOrderStatisticsInfoDateResult> getStatInfoOfDate(String startDateStr, String endDateStr) {
        Date startDate = DateUtil.parseDate(startDateStr);
        Date endDate = DateUtil.parseDate(endDateStr);

        return orderStatisticsInfoDao.getStatInfoOfDate(startDate,endDate);
    }
}
