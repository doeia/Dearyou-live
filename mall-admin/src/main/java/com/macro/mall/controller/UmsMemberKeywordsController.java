package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMemberKeywords;
import com.macro.mall.service.UmsMemberKeywordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员与关键词表（用户搜索的关键词）
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
@Controller
@Api(tags = "UmsMemberKeywordsController", description ="会员与关键词表（用户搜索的关键词）")
@RequestMapping("/memberKeywords")
public class UmsMemberKeywordsController {

    @Autowired
    private UmsMemberKeywordsService memberKeywordsService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberKeywords:list')")
    public CommonResult<CommonPage<UmsMemberKeywords>> list(@RequestParam(value = "memberId", required = false) Long memberId,
                                                            @RequestParam(value = "keywords", required = false) String keywords,
                                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsMemberKeywords> list = memberKeywordsService.list(memberId, keywords, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ums:memberKeywords:read','ums:memberKeywords:update')")
    public CommonResult<UmsMemberKeywords> getDetail(@PathVariable Long id) {

        UmsMemberKeywords memberKeywords = memberKeywordsService.getItem(id);
        if(memberKeywords == null){
            return CommonResult.failed();
        }

        return CommonResult.success(memberKeywords);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberKeywords:create')")
    public CommonResult create(@RequestBody UmsMemberKeywords memberKeywords) {
        CommonResult commonResult;
        int count = memberKeywordsService.create(memberKeywords);
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
    @PreAuthorize("hasAuthority('ums:memberKeywords:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberKeywords memberKeywords) {
        int count = memberKeywordsService.update(id, memberKeywords);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberKeywords:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = memberKeywordsService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:memberKeywords:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = memberKeywordsService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}