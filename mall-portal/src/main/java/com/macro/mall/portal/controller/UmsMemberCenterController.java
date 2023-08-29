package com.macro.mall.portal.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.domain.MemberCenterResult;
import com.macro.mall.portal.domain.MemberDetailParam;
import com.macro.mall.portal.domain.MemberDetailResult;
import com.macro.mall.portal.service.OmsCartItemService;
import com.macro.mall.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 会员个人中心Controller
 */
@Controller
@Api(tags = "UmsMemberController", description = "会员登录注册管理")
@RequestMapping("/member/center")
public class UmsMemberCenterController {
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("个人中心")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<MemberCenterResult> info() {
        UmsMember umsMember = memberService.getCurrentMember();
        Long cartCount = cartItemService.getCartCount(umsMember.getId());
        MemberCenterResult memberCenterResult = new MemberCenterResult();
        memberCenterResult.setAvatarUrl(umsMember.getIcon());
        memberCenterResult.setNickName(umsMember.getNickname());
        memberCenterResult.setCartCount(cartCount);

        return CommonResult.success(memberCenterResult);
    }

    @ApiOperation("个人详细信息")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<MemberDetailResult> detail() {
        UmsMember umsMember = memberService.getCurrentMember();
        MemberDetailResult memberDetailResult = new MemberDetailResult();
        memberDetailResult.setAvatarUrl(umsMember.getIcon());
        memberDetailResult.setNickName(umsMember.getNickname());
        memberDetailResult.setPhone(umsMember.getPhone());
        memberDetailResult.setIdCard(umsMember.getIdCard());

        return CommonResult.success(memberDetailResult);
    }

    @ApiOperation("修改会员信息")
    @RequestMapping(value = "/updateMemberInfo", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateMemberInfo(@RequestBody MemberDetailParam memberDetailParam) {
        return memberService.updateMemberInfo(memberDetailParam);
    }

    @ApiOperation("修改身份证")
    @RequestMapping(value = "/updateIdCard", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateIdCard(@RequestParam String realName,
                                     @RequestParam String idCard) {
        return memberService.updateIdCard(realName, idCard);
    }
}
