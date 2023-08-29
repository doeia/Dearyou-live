package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.UmsMemberQueryParam;
import com.macro.mall.model.UmsMember;
import com.macro.mall.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "UmsMemberController", description = "会员管理")
@RequestMapping("/member")
public class UmsMemberController {

    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("根据分页获取会员列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:member:list')")
    public CommonResult<CommonPage<UmsMember>> list(UmsMemberQueryParam memberQueryParam,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsMember> memberList = memberService.list(memberQueryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(memberList));
    }

    @ApiOperation("获取指定会员信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ums:member:read','ums:member:update')")
    public CommonResult<UmsMember> getDetail(@PathVariable Long id) {

        UmsMember umsMember = memberService.getItem(id);
        return CommonResult.success(umsMember);
    }

    @ApiOperation(value = "添加会员")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:member:create')")
    public CommonResult create(@RequestBody UmsMember member) {
        CommonResult commonResult;
        int count = memberService.create(member);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @ApiOperation("修改指定会员信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:member:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMember member) {
        int count = memberService.update(id, member);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改会员状态")
    @RequestMapping(value = "/update/status", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ums:member:update')")
    public CommonResult updateStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        int count = memberService.updateStatus(ids, status);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
