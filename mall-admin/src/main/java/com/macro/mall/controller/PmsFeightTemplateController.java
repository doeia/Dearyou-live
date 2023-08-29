package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.PmsFeightTemplate;
import com.macro.mall.service.PmsFeightTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运费模版
 *
 * @author taobao
 */
@Controller
@Api(tags = "PmsFeightTemplateController", description ="运费模版")
@RequestMapping("/feightTemplate")
public class PmsFeightTemplateController {

    @Autowired
    private PmsFeightTemplateService feightTemplateService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:feightTemplate:list')")
    public CommonResult<CommonPage<PmsFeightTemplate>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsFeightTemplate> list = feightTemplateService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:feightTemplate:read')")
    public CommonResult<PmsFeightTemplate> getDetail(@PathVariable Long id) {

        PmsFeightTemplate feightTemplate = feightTemplateService.getItem(id);
        if(feightTemplate == null){
            return CommonResult.failed();
        }

        return CommonResult.success(feightTemplate);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:feightTemplate:create')")
    public CommonResult create(@RequestBody PmsFeightTemplate feightTemplate) {
        CommonResult commonResult;
        int count = feightTemplateService.create(feightTemplate);
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
    @PreAuthorize("hasAuthority('pms:feightTemplate:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsFeightTemplate feightTemplate) {
        int count = feightTemplateService.update(id, feightTemplate);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:feightTemplate:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = feightTemplateService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:feightTemplate:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = feightTemplateService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}