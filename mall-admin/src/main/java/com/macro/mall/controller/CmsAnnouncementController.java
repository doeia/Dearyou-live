package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.CmsAnnouncement;
import com.macro.mall.service.CmsAnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "CmsAnnouncementController", description = "公告管理")
@RequestMapping("/announcement")
public class CmsAnnouncementController {

    @Autowired
    private CmsAnnouncementService announcementService;

    @ApiOperation("根据分页获取公告列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('cms:announcement:list')")
    public CommonResult<CommonPage<CmsAnnouncement>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsAnnouncement> list = announcementService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation(value = "根据编号查询公告详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('cms:announcement:read','cms:announcement:update')")
    public CommonResult<CmsAnnouncement> getDetail(@PathVariable Long id) {

        CmsAnnouncement announcement = announcementService.getItem(id);
        if(announcement == null){
            return CommonResult.failed();
        }

        return CommonResult.success(announcement);
    }

    @ApiOperation(value = "添加公告")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('cms:announcement:create')")
    public CommonResult create(@Validated @RequestBody CmsAnnouncement announcement, BindingResult result) {
        CommonResult commonResult;
        int count = announcementService.create(announcement);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @ApiOperation("修改指定公告信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('cms:announcement:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody CmsAnnouncement announcement) {
        int count = announcementService.update(id, announcement);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除指定公告")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('cms:announcement:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = announcementService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('cms:announcement:deleteAll')")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = announcementService.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
