package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.SmsArticle;
import com.macro.mall.service.SmsArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章表
 *
 * @author taobao
 * @date Sun Nov 17 16:44:14 CST 2019
 */
@Controller
@Api(tags = "SmsArticleController", description ="文章表")
@RequestMapping("/article")
public class SmsArticleController {

    @Autowired
    private SmsArticleService articleService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('sms:article:list')")
    public CommonResult<CommonPage<SmsArticle>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsArticle> list = articleService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('sms:article:read')")
    public CommonResult<SmsArticle> getDetail(@PathVariable Long id) {

        SmsArticle article = articleService.getItem(id);
        if(article == null){
            return CommonResult.failed();
        }

        return CommonResult.success(article);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('sms:article:create')")
    public CommonResult create(@RequestBody SmsArticle article) {
        CommonResult commonResult;
        int count = articleService.create(article);
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
    @PreAuthorize("hasAuthority('sms:article:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsArticle article) {
        int count = articleService.update(id, article);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('sms:article:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = articleService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('sms:article:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = articleService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}