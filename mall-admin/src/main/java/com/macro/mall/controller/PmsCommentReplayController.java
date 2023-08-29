package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.PmsCommentReplayQueryParam;
import com.macro.mall.model.PmsCommentReplay;
import com.macro.mall.service.PmsCommentReplayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品评价回复表
 *
 * @author taobao
 */
@Controller
@Api(tags = "PmsCommentReplayController", description ="产品评价回复表")
@RequestMapping("/commentReplay")
public class PmsCommentReplayController {

    @Autowired
    private PmsCommentReplayService commentReplayService;

    @ApiOperation("根据分页获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:commentReplay:list')")
    public CommonResult<CommonPage<PmsCommentReplay>> list(PmsCommentReplayQueryParam queryParam,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsCommentReplay> list = commentReplayService.list(queryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:commentReplay:read')")
    public CommonResult<PmsCommentReplay> getDetail(@PathVariable Long id) {

        PmsCommentReplay commentReplay = commentReplayService.getItem(id);
        if(commentReplay == null){
            return CommonResult.failed();
        }

        return CommonResult.success(commentReplay);
    }

    @ApiOperation(value = "添加信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:commentReplay:create')")
    public CommonResult create(@RequestBody PmsCommentReplay commentReplay) {
        CommonResult commonResult;
        int count = commentReplayService.create(commentReplay);
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
    @PreAuthorize("hasAuthority('pms:commentReplay:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsCommentReplay commentReplay) {
        int count = commentReplayService.update(id, commentReplay);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除记录")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:commentReplay:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = commentReplayService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:commentReplay:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = commentReplayService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}