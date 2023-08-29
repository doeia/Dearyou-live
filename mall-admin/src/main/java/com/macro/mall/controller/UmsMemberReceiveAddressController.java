package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMemberReceiveAddress;
import com.macro.mall.service.UmsMemberReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author bobo
 * 收货地址
 */
@Controller
@Api(tags = "UmsMemberReceiveAddressController", description = "收货地址")
@RequestMapping("/memberReceiveAddress")
public class UmsMemberReceiveAddressController {

    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;

    @ApiOperation("根据分页获取会员收获地址列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsMemberReceiveAddress>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsMemberReceiveAddress> memberList = memberReceiveAddressService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(memberList));
    }

    @ApiOperation("获取指定收货地址信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsMemberReceiveAddress> getDetail(@PathVariable Long id) {

        UmsMemberReceiveAddress memberReceiveAddress = memberReceiveAddressService.getItem(id);
        if(memberReceiveAddress == null){
            return CommonResult.failed();
        }

        return CommonResult.success(memberReceiveAddress);
    }
}
