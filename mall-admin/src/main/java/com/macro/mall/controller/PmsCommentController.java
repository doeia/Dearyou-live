package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.PmsCommentQueryParam;
import com.macro.mall.model.PmsComment;
import com.macro.mall.service.PmsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品评价表
 *
 * @author taobao
 */
@Controller
@Api(tags = "PmsCommentController", description ="商品评价表")
@RequestMapping("/comment")
public class PmsCommentController {

    @Autowired
    private PmsCommentService commentService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:comment:list')")
    public CommonResult<CommonPage<PmsComment>> list(PmsCommentQueryParam queryParam,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsComment> list = commentService.list(queryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:comment:read')")
    public CommonResult<PmsComment> getDetail(@PathVariable Long id) {

        PmsComment comment = commentService.getItem(id);
        if(comment == null){
            return CommonResult.failed();
        }

        return CommonResult.success(comment);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:comment:create')")
    public CommonResult create(@RequestBody PmsComment comment) {
        CommonResult commonResult;
        int count = commentService.create(comment);
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
    @PreAuthorize("hasAuthority('pms:comment:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsComment comment) {
        int count = commentService.update(id, comment);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:comment:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = commentService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:comment:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = commentService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("隐藏")
    @RequestMapping(value = "/hide/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:comment:status')")
    public CommonResult hide(@PathVariable Long id) {
        int count = commentService.updateStatus(id, 0);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("显示")
    @RequestMapping(value = "/show/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:comment:status')")
    public CommonResult show(@PathVariable Long id) {
        int count = commentService.updateStatus(id, 1);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}