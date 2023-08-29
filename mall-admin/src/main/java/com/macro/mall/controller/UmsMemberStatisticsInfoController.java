package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoNumberResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoSexResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoTimeResult;
import com.macro.mall.model.UmsMemberStatisticsInfo;
import com.macro.mall.service.UmsMemberStatisticsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员统计信息
 *
 * @author taobao
 * @date Sun Nov 17 11:02:46 CST 2019
 */
@Controller
@Api(tags = "UmsMemberStatisticsInfoController", description ="会员统计信息")
@RequestMapping("/memberStatisticsInfo")
public class UmsMemberStatisticsInfoController {

    @Autowired
    private UmsMemberStatisticsInfoService memberStatisticsInfoService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:list')")
    public CommonResult<CommonPage<UmsMemberStatisticsInfo>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsMemberStatisticsInfo> list = memberStatisticsInfoService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:read')")
    public CommonResult<UmsMemberStatisticsInfo> getDetail(@PathVariable Long id) {

        UmsMemberStatisticsInfo memberStatisticsInfo = memberStatisticsInfoService.getItem(id);
        return CommonResult.success(memberStatisticsInfo);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:create')")
    public CommonResult create(@RequestBody UmsMemberStatisticsInfo memberStatisticsInfo) {
        CommonResult commonResult;
        int count = memberStatisticsInfoService.create(memberStatisticsInfo);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @ApiOperation("修改信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberStatisticsInfo memberStatisticsInfo) {
        int count = memberStatisticsInfoService.update(id, memberStatisticsInfo);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = memberStatisticsInfoService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = memberStatisticsInfoService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取会员统计信息")
    @RequestMapping(value = "/getStatInfo/{memberId}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:read')")
    public CommonResult<UmsMemberStatisticsInfo> getStatInfo(@PathVariable Long memberId) {
        UmsMemberStatisticsInfo memberStatisticsInfo = memberStatisticsInfoService.getStatInfo(memberId);
        return CommonResult.success(memberStatisticsInfo);
    }

    @ApiOperation("获取会员男女比例信息")
    @RequestMapping(value = "/getStatInfoOfSex", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:read')")
    public CommonResult<UmsMemberStatisticsInfoSexResult> getStatInfoOfSex() {
        UmsMemberStatisticsInfoSexResult result = memberStatisticsInfoService.getStatInfoOfSex();
        return CommonResult.success(result);
    }

    @ApiOperation("按时间获取信息")
    @RequestMapping(value = "/getStatInfoOfTime", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:read')")
    public CommonResult<List<UmsMemberStatisticsInfoTimeResult>> getStatInfoOfTime(@RequestParam(value = "mode", defaultValue = "1") Integer mode, @RequestParam(name = "date", defaultValue = "2019-11-23") String date) {
        List<UmsMemberStatisticsInfoTimeResult> list = memberStatisticsInfoService.getStatInfoOfTime(mode, date);
        return CommonResult.success(list);
    }

    @ApiOperation("获取会员数量统计信息")
    @RequestMapping(value = "/getStatInfoOfNumber", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("hasAuthority('ums:memberStatisticsInfo:read')")
    public CommonResult<UmsMemberStatisticsInfoNumberResult> getStatInfoOfNumber() {
        UmsMemberStatisticsInfoNumberResult result = memberStatisticsInfoService.getStatInfoOfNumber();
        return CommonResult.success(result);
    }
}