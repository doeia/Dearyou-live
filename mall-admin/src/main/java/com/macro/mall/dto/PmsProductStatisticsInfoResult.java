package com.macro.mall.dto;

import lombok.Data;

@Data
public class PmsProductStatisticsInfoResult {
    private Integer onShelfTotal;
    private Integer offShelfTotal;
    private Integer warnStockTotal;
    private Integer goodsTotal;
}
