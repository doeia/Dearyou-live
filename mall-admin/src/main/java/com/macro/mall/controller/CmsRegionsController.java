package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.CmsRegionsNode;
import com.macro.mall.dto.CmsRegionsParam;
import com.macro.mall.model.CmsRegions;
import com.macro.mall.model.UmsAdmin;
import com.macro.mall.service.CmsRegionsService;
import com.macro.mall.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 区域表
 *
 * @author taobao
 */
@Controller
@Api(tags = "CmsRegionsController", description ="区域表")
@RequestMapping("/regions")
public class CmsRegionsController {

    @Autowired
    private CmsRegionsService regionsService;
    @Autowired
    private UmsAdminService adminService;

    @ApiOperation("获取树型列表")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('cms:regions:list')")
    public CommonResult<CommonPage<CmsRegionsNode>> treeList() {
        List<CmsRegionsNode> list = regionsService.treeList();
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('cms:regions:read','cms:regions:update')")
    public CommonResult<CmsRegions> getDetail(@PathVariable Long id) {

        CmsRegions regions = regionsService.getItem(id);
        if(regions == null){
            return CommonResult.failed();
        }

        return CommonResult.success(regions);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('cms:regions:create')")
    public CommonResult create(Principal principal, @Validated @RequestBody CmsRegionsParam regions, BindingResult result) {
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        if(umsAdmin==null){
            return CommonResult.failed();
        }
        CommonResult commonResult;
        int count = regionsService.create(regions, umsAdmin.getId(),username);
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
    @PreAuthorize("hasAuthority('cms:regions:update')")
    public CommonResult update(@PathVariable Long id, Principal principal, @RequestBody CmsRegionsParam regions) {
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        if(umsAdmin==null){
            return CommonResult.failed();
        }

        int count = regionsService.update(id, regions,umsAdmin.getId(),username);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("丢弃记录")
    @RequestMapping(value = "/drop/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('cms:regions:drop')")
    public CommonResult drop(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        if(umsAdmin==null){
            return CommonResult.failed();
        }

        int count = regionsService.drop(id, umsAdmin.getId(),username);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}