package com.macro.mall.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OmsOrderStatisticsInfoResult {
    private Integer todayOrderTotal;
    private BigDecimal todayOrderAmount;
    private Integer yesterdayOrderTotal;
    private BigDecimal yesterdayOrderAmount;
    private Integer orderPayingTotal;
    private Integer orderDeliveringTotal;
    private Integer orderDeliveredTotal;
    private Integer orderCompletedTotal;
    private Integer orderConfirmTotal;
    private Integer orderRefundApplyTotal;
    private Integer orderRefundReturnTotal;

    private Integer thisMonthOrderTotal;
    private Integer thisWeekOrderTotal;
    private BigDecimal thisMonthOrderAmount;
    private BigDecimal thisWeekOrderAmount;
    private Integer lastMonthOrderTotal;
    private Integer lastWeekOrderTotal;
    private BigDecimal lastMonthOrderAmount;
    private BigDecimal lastWeekOrderAmount;

}
