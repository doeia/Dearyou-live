package com.macro.mall.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OmsOrderStatisticsInfoDateResult {
    private BigDecimal orderAmount;
    private Integer orderCount;
    private String date;
}
