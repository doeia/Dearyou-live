package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.PmsProductStatisticsInfoResult;
import com.macro.mall.service.PmsProductStatisticsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品统计信息
 *
 * @author taobao
 * @date Sun Nov 17 11:02:46 CST 2019
 */
@Controller
@Api(tags = "PmsProductStatisticsInfoController", description ="商品统计信息")
@RequestMapping("/productStatisticsInfo")
public class PmsProductStatisticsInfoController {
    @Autowired
    private PmsProductStatisticsInfoService productStatisticsInfoService;

    @ApiOperation("获取商品统计信息")
    @RequestMapping(value = "/getStatInfo", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("hasAuthority('pms:productStatisticsInfo:read')")
    public CommonResult<PmsProductStatisticsInfoResult> getStatInfo() {
        PmsProductStatisticsInfoResult result = productStatisticsInfoService.getStatInfo();
        return CommonResult.success(result);
    }
}
