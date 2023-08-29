package com.macro.mall.service.impl;

import com.macro.mall.dao.PmsProductStatisticsInfoDao;
import com.macro.mall.dto.PmsProductStatisticsInfoResult;
import com.macro.mall.service.PmsProductStatisticsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PmsProductStatisticsInfoServiceImpl implements PmsProductStatisticsInfoService {
    @Autowired
    private PmsProductStatisticsInfoDao productStatisticsInfoDao;

    @Override
    public PmsProductStatisticsInfoResult getStatInfo() {
        return productStatisticsInfoDao.getStatInfo();
    }
}
