package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.PmsBrandCategory;
import com.macro.mall.service.PmsBrandCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌分类表
 *
 * @author taobao
 * @date Sat Nov 16 21:46:24 CST 2019
 */
@Controller
@Api(tags = "PmsBrandCategoryController", description ="品牌分类")
@RequestMapping("/brandCategory")
public class PmsBrandCategoryController {

    @Autowired
    private PmsBrandCategoryService brandCategoryService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brandCategory:list')")
    public CommonResult<CommonPage<PmsBrandCategory>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsBrandCategory> list = brandCategoryService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brandCategory:read')")
    public CommonResult<PmsBrandCategory> getDetail(@PathVariable Long id) {

        PmsBrandCategory brandCategory = brandCategoryService.getItem(id);
        if(brandCategory == null){
            return CommonResult.failed();
        }

        return CommonResult.success(brandCategory);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brandCategory:create')")
    public CommonResult create(@RequestBody PmsBrandCategory brandCategory) {
        CommonResult commonResult;
        int count = brandCategoryService.create(brandCategory);
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
    @PreAuthorize("hasAuthority('pms:brandCategory:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsBrandCategory brandCategory) {
        int count = brandCategoryService.update(id, brandCategory);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brandCategory:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = brandCategoryService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brandCategory:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = brandCategoryService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("查询所有品牌分类")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brandCategory:read')")
    public CommonResult<List<PmsBrandCategory>> listAll() {
        List<PmsBrandCategory> list = brandCategoryService.list();
        return CommonResult.success(list);
    }
}