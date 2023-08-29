package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsAdminLoginLog;
import com.macro.mall.service.UmsAdminLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台用户登录日志表
 *
 * @author taobao
 * @date Sun Nov 17 16:44:14 CST 2019
 */
@Controller
@Api(tags = "UmsAdminLoginLogController", description ="后台用户登录日志表")
@RequestMapping("/adminLoginLog")
public class UmsAdminLoginLogController {

    @Autowired
    private UmsAdminLoginLogService adminLoginLogService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:adminLoginLog:list')")
    public CommonResult<CommonPage<UmsAdminLoginLog>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsAdminLoginLog> list = adminLoginLogService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ums:adminLoginLog:read','ums:adminLoginLog:update')")
    public CommonResult<UmsAdminLoginLog> getDetail(@PathVariable Long id) {

        UmsAdminLoginLog adminLoginLog = adminLoginLogService.getItem(id);
        if(adminLoginLog == null){
            return CommonResult.failed();
        }

        return CommonResult.success(adminLoginLog);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:adminLoginLog:create')")
    public CommonResult create(@RequestBody UmsAdminLoginLog adminLoginLog) {
        CommonResult commonResult;
        int count = adminLoginLogService.create(adminLoginLog);
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
    @PreAuthorize("hasAuthority('ums:adminLoginLog:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsAdminLoginLog adminLoginLog) {
        int count = adminLoginLogService.update(id, adminLoginLog);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:adminLoginLog:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = adminLoginLogService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:adminLoginLog:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = adminLoginLogService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}