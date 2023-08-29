package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMemberLevel;
import com.macro.mall.service.UmsMemberLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员等级表
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
@Controller
@Api(tags = "UmsMemberLevelController", description ="会员等级表")
@RequestMapping("/memberLevel")
public class UmsMemberLevelController {

    @Autowired
    private UmsMemberLevelService memberLevelService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberLevel:list')")
    public CommonResult<CommonPage<UmsMemberLevel>> list() {
        List<UmsMemberLevel> list = memberLevelService.list();
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ums:memberLevel:read','ums:memberLevel:update')")
    public CommonResult<UmsMemberLevel> getDetail(@PathVariable Long id) {

        UmsMemberLevel memberLevel = memberLevelService.getItem(id);
        if(memberLevel == null){
            return CommonResult.failed();
        }

        return CommonResult.success(memberLevel);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberLevel:create')")
    public CommonResult create(@RequestBody UmsMemberLevel memberLevel) {
        CommonResult commonResult;
        int count = memberLevelService.create(memberLevel);
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
    @PreAuthorize("hasAuthority('ums:memberLevel:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberLevel memberLevel) {
        int count = memberLevelService.update(id, memberLevel);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberLevel:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = memberLevelService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberLevel:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = memberLevelService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}