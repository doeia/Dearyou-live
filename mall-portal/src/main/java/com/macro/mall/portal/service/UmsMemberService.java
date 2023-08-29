package com.macro.mall.portal.service;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMember;
import com.macro.mall.portal.domain.MemberDetailParam;
import com.macro.mall.portal.domain.WxUserResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员管理Service
 * Created by macro on 2018/8/3.
 */
public interface UmsMemberService {
    /**
     * 根据用户名获取会员
     */
    UmsMember getByUsername(String username);

    /**
     * 根据会员编号获取会员
     */
    UmsMember getById(Long id);

    /**
     * 用户注册
     */
    @Transactional
    CommonResult register(String username, String password, String telephone, String authCode);

    /**
     * 用户注册
     */
    @Transactional
    CommonResult<WxUserResult> register(WxMaUserInfo userInfo);

    /**
     * 生成验证码
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 修改手机号码
     */
    @Transactional
    CommonResult updatePhone(String telephone, String authCode);

    /**
     * 修改身份证
     */
    @Transactional
    CommonResult updateIdCard(String realName, String idCard);

    /**
     * 修改会员信息
     */
    @Transactional
    CommonResult updateMemberInfo(MemberDetailParam memberDetailParam);

    /**
     * 修改密码
     */
    @Transactional
    CommonResult updatePassword(String telephone, String password, String authCode);

    /**
     * 获取当前登录会员
     */
    UmsMember getCurrentMember();

    /**
     * 根据会员id修改会员积分
     */
    void updateIntegration(Long id,Integer integration);
}
