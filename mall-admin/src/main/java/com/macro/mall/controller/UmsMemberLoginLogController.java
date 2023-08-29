package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMemberLoginLog;
import com.macro.mall.service.UmsMemberLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员登录记录
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
@Controller
@Api(tags = "UmsMemberLoginLogController", description ="会员登录记录")
@RequestMapping("/memberLoginLog")
public class UmsMemberLoginLogController {

    @Autowired
    private UmsMemberLoginLogService memberLoginLogService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberLoginLog:list')")
    public CommonResult<CommonPage<UmsMemberLoginLog>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsMemberLoginLog> list = memberLoginLogService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ums:memberLoginLog:read','ums:memberLoginLog:update')")
    public CommonResult<UmsMemberLoginLog> getDetail(@PathVariable Long id) {

        UmsMemberLoginLog memberLoginLog = memberLoginLogService.getItem(id);
        if(memberLoginLog == null){
            return CommonResult.failed();
        }

        return CommonResult.success(memberLoginLog);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberLoginLog:create')")
    public CommonResult create(@RequestBody UmsMemberLoginLog memberLoginLog) {
        CommonResult commonResult;
        int count = memberLoginLogService.create(memberLoginLog);
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
    @PreAuthorize("hasAuthority('ums:memberLoginLog:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberLoginLog memberLoginLog) {
        int count = memberLoginLogService.update(id, memberLoginLog);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberLoginLog:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = memberLoginLogService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberLoginLog:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = memberLoginLogService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}