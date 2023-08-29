package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.OmsOrderStatisticsInfoDateResult;
import com.macro.mall.dto.OmsOrderStatisticsInfoResult;
import com.macro.mall.service.OmsOrderStatisticsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 订单统计信息
 *
 * @author taobao
 * @date Sun Nov 17 11:02:46 CST 2019
 */
@Controller
@Api(tags = "OmsOrderStatisticsInfoController", description ="订单统计信息")
@RequestMapping("/orderStatisticsInfo")
public class OmsOrderStatisticsInfoController {
    @Autowired
    private OmsOrderStatisticsInfoService orderStatisticsInfoService;

    @ApiOperation("获取订单统计信息")
    @RequestMapping(value = "/getStatInfo", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("hasAuthority('oms:orderStatisticsInfo:read')")
    public CommonResult<OmsOrderStatisticsInfoResult> getStatInfo() {
        OmsOrderStatisticsInfoResult result = orderStatisticsInfoService.getStatInfo();
        return CommonResult.success(result);
    }

    @ApiOperation("按日期范围获取订单统计信息")
    @RequestMapping(value = "/getStatInfoOfDate", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<OmsOrderStatisticsInfoDateResult>> getStatInfoOfDate(@RequestParam(value = "startDate", defaultValue = "") String startDate,
                                          @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        List<OmsOrderStatisticsInfoDateResult> resultList = orderStatisticsInfoService.getStatInfoOfDate(startDate, endDate);
        return CommonResult.success(resultList);
    }
}
