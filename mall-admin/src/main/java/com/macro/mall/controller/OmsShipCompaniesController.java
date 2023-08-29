package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.OmsShipCompanies;
import com.macro.mall.service.OmsShipCompaniesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配送公司表
 *
 * @author taobao
 */
@Controller
@Api(tags = "OmsShipCompaniesController", description ="配送公司表")
@RequestMapping("/shipCompanies")
public class OmsShipCompaniesController {

    @Autowired
    private OmsShipCompaniesService shipCompaniesService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('oms:shipCompanies:list')")
    public CommonResult<CommonPage<OmsShipCompanies>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsShipCompanies> list = shipCompaniesService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('oms:shipCompanies:read')")
    public CommonResult<OmsShipCompanies> getDetail(@PathVariable Long id) {

        OmsShipCompanies shipCompanies = shipCompaniesService.getItem(id);
        if(shipCompanies == null){
            return CommonResult.failed();
        }

        return CommonResult.success(shipCompanies);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('oms:shipCompanies:create')")
    public CommonResult create(@RequestBody OmsShipCompanies shipCompanies) {
        CommonResult commonResult;
        int count = shipCompaniesService.create(shipCompanies);
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
    @PreAuthorize("hasAuthority('oms:shipCompanies:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody OmsShipCompanies shipCompanies) {
        int count = shipCompaniesService.update(id, shipCompanies);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('oms:shipCompanies:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = shipCompaniesService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('oms:shipCompanies:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = shipCompaniesService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}