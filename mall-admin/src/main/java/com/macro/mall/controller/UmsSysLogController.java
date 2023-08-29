package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsSysLog;
import com.macro.mall.service.UmsSysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "UmsSysLogController", description = "日志管理")
@RequestMapping("/sysLog")
public class UmsSysLogController {

    @Autowired
    private UmsSysLogService sysLogService;

    @ApiOperation("根据分页获取日志列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:sysLog:list')")
    public CommonResult<CommonPage<UmsSysLog>> list(@RequestParam(value = "admin", required = false) String admin,
                                                    @RequestParam(value = "deleted", defaultValue = "0") Integer deleted,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsSysLog> sysLogList = sysLogService.list(admin,deleted, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(sysLogList));
    }

    @ApiOperation(value = "根据编号查询日志信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:sysLog:read')")
    public CommonResult<UmsSysLog> getDetail(@PathVariable Long id) {

        UmsSysLog sysLog = sysLogService.getItem(id);
        if(sysLog == null){
            return CommonResult.failed();
        }

        return CommonResult.success(sysLog);
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:sysLog:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = sysLogService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:sysLog:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = sysLogService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("丢弃记录")
    @RequestMapping(value = "/drop/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:sysLog:drop')")
    public CommonResult drop(@PathVariable Long id) {
        int count = sysLogService.drop(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量丢弃")
    @RequestMapping(value = "/drop", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:sysLog:dropAll')")
    public CommonResult drop(@RequestParam("ids") List<Long> ids) {
        int count = sysLogService.drop(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("还原记录")
    @RequestMapping(value = "/recover/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:sysLog:recover')")
    public CommonResult recover(@PathVariable Long id) {
        int count = sysLogService.recover(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量还原")
    @RequestMapping(value = "/recover", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:sysLog:recoverAll')")
    public CommonResult recover(@RequestParam("ids") List<Long> ids) {
        int count = sysLogService.recover(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
