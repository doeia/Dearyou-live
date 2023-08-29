package com.macro.mall.dto;

import lombok.Data;

@Data
public class UmsMemberStatisticsInfoNumberResult {
    private Integer todayNumber;
    private Integer yesterdayNumber;
    private Integer instantNumber;
    private Integer totalNumber;
}
