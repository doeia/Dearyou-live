package com.macro.mall.dto;

import lombok.Data;

import java.util.List;


@Data
public class ExpressResult {
    private Integer state;
    private List<TimeLine> data;

    @Data
    public static class TimeLine{
        private String time;
        private String context;
    }
}
