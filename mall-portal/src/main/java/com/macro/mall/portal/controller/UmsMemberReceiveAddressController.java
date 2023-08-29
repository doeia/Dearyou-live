package com.macro.mall.portal.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMemberReceiveAddress;
import com.macro.mall.portal.domain.PageParam;
import com.macro.mall.portal.service.UmsMemberReceiveAddressService;
import com.macro.mall.portal.util.DataValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员收货地址管理Controller
 * Created by macro on 2018/8/28.
 */
@Controller
@Api(tags = "UmsMemberReceiveAddressController", description = "会员收货地址管理")
@RequestMapping("/member/address")
public class UmsMemberReceiveAddressController {
    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;

    @ApiOperation("添加收货地址")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(@RequestBody UmsMemberReceiveAddress address) {
        //验证手机号码是否正确
        if(!DataValidateUtil.isPhone(address.getPhoneNumber())){
            return CommonResult.failed("请输入正确的手机号码");
        }

        int count = memberReceiveAddressService.add(address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除收货地址")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = memberReceiveAddressService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改收货地址")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberReceiveAddress address) {
        //验证手机号码是否正确
        if(!DataValidateUtil.isPhone(address.getPhoneNumber())){
            return CommonResult.failed("请输入正确的手机号码");
        }

        int count = memberReceiveAddressService.update(id, address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<UmsMemberReceiveAddress>> list(@RequestBody PageParam page) {
        List<UmsMemberReceiveAddress> addressList = memberReceiveAddressService.list(page);
        return CommonResult.success(CommonPage.restPage(addressList));

    }

    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable Long id) {
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(id);
        return CommonResult.success(address);
    }

    @ApiOperation("设置默认地址")
    @RequestMapping(value = "/setDefaultAddress/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult setDefaultAddress(@PathVariable Long id) {
        int count = memberReceiveAddressService.setDefaultAddress(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

}
