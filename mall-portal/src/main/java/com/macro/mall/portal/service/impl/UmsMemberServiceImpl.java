package com.macro.mall.portal.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.*;
import com.macro.mall.model.*;
import com.macro.mall.portal.domain.MemberDetailParam;
import com.macro.mall.portal.domain.MemberDetails;
import com.macro.mall.portal.domain.WxUserResult;
import com.macro.mall.portal.service.RedisService;
import com.macro.mall.portal.service.UmsMemberService;
import com.macro.mall.portal.util.DataValidateUtil;
import com.macro.mall.portal.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 会员管理Service实现类
 * Created by macro on 2018/8/3.
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    @Autowired
    private UmsMemberMapper memberMapper;
    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;
    @Autowired
    private UmsMemberOauthMapper memberOauthMapper;
    @Autowired
    private UmsMemberLoginLogMapper loginLogMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UmsMemberStatisticsInfoMapper memberStatisticsInfoMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public UmsMember getByUsername(String username) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            return memberList.get(0);
        }
        return null;
    }

    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public CommonResult register(String username, String password, String telephone, String authCode) {
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            return CommonResult.failed("验证码错误");
        }
        //查询是否已有该用户
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        example.or(example.createCriteria().andPhoneEqualTo(telephone));
        List<UmsMember> umsMembers = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(umsMembers)) {
            return CommonResult.failed("该用户已经存在");
        }
        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        umsMember.setPhone(telephone);
        umsMember.setPassword(passwordEncoder.encode(password));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        //获取默认会员等级并设置
        UmsMemberLevelExample levelExample = new UmsMemberLevelExample();
        levelExample.createCriteria().andDefaultStatusEqualTo(1);
        List<UmsMemberLevel> memberLevelList = memberLevelMapper.selectByExample(levelExample);
        if (!CollectionUtils.isEmpty(memberLevelList)) {
            umsMember.setMemberLevelId(memberLevelList.get(0).getId());
        }
        memberMapper.insert(umsMember);

        //插入会员统计信息
        UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
        umsMemberStatisticsInfo.setMemberId(umsMember.getId());
        umsMemberStatisticsInfo.setConsumeAmount(new BigDecimal(0));
        umsMemberStatisticsInfo.setOrderCount(0);
        umsMemberStatisticsInfo.setCouponCount(0);
        umsMemberStatisticsInfo.setCommentCount(0);
        umsMemberStatisticsInfo.setReturnOrderCount(0);
        umsMemberStatisticsInfo.setLoginCount(0);
        umsMemberStatisticsInfo.setAttendCount(0);
        umsMemberStatisticsInfo.setFansCount(0);
        umsMemberStatisticsInfo.setCollectProductCount(0);
        umsMemberStatisticsInfo.setCollectSubjectCount(0);
        umsMemberStatisticsInfo.setCollectTopicCount(0);
        umsMemberStatisticsInfo.setCollectCommentCount(0);
        umsMemberStatisticsInfo.setInviteFriendCount(0);
        memberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);

        umsMember.setPassword(null);
        return CommonResult.success(null, "注册成功");
    }

    @Override
    public CommonResult updatePhone(String telephone, String authCode) {
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            return CommonResult.failed("验证码错误");
        }

        //验证手机号码是否正确
        if(!DataValidateUtil.isPhone(telephone)){
            return CommonResult.failed("请输入正确的手机号码");
        }

        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(telephone);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            return CommonResult.failed("该手机号已被使用");
        }

        //获取当前用户
        UmsMember umsMember = getCurrentMember();
        umsMember.setPhone(telephone);
        memberMapper.updateByPrimaryKeySelective(umsMember);
        return CommonResult.success(null, "修改手机号成功");
    }

    @Override
    public CommonResult updateIdCard(String realName, String idCard) {
        //验证身份证和真实姓名
        if(StringUtils.isEmpty(realName)){
            return CommonResult.failed("请填写真实姓名");
        }
        if(StringUtils.isEmpty(idCard)){
            return CommonResult.failed("请填写身份证号码");
        }

        //验证身份证是否正确
        if(!IdcardUtil.isValidCard(idCard)){
            return CommonResult.failed("请输入正确的身份证");
        }

        //获取当前用户
        UmsMember umsMember = getCurrentMember();
        umsMember.setIdCard(idCard);
        umsMember.setRealName(realName);
        memberMapper.updateByPrimaryKeySelective(umsMember);
        return CommonResult.success(null, "修改身份证成功");
    }

    @Override
    public CommonResult updateMemberInfo(MemberDetailParam memberDetailParam) {
        //验证身份证是否正确
        if(!IdcardUtil.isValidCard(memberDetailParam.getIdCard())){
            return CommonResult.failed("请输入正确的身份证");
        }

        //获取当前用户
        UmsMember umsMember = getCurrentMember();
        umsMember.setIcon(memberDetailParam.getAvatarUrl());
        umsMember.setNickname(memberDetailParam.getNickName());
        umsMember.setIdCard(memberDetailParam.getIdCard());

        memberMapper.updateByPrimaryKeySelective(umsMember);
        return CommonResult.success(null, "修改会员信息成功");
    }

    @Override
    public CommonResult<WxUserResult> register(WxMaUserInfo userInfo) {
        //查询是否已有该用户
        String server = "WeiXinMiniProgram";
        int sourceType = 3;//0->PC；1->android;2->ios;3->微信小程序
        WxUserResult result = new WxUserResult();
        UmsMemberOauthExample example = new UmsMemberOauthExample();
        example.createCriteria().andOpenIdEqualTo(userInfo.getOpenId()).andServerEqualTo(server);
        List<UmsMemberOauth> umsMemberOauths = memberOauthMapper.selectByExample(example);

        if (!CollectionUtils.isEmpty(umsMemberOauths)) {
            //用户已存在，生成token返回
            UmsMemberOauth umsMemberOauth = umsMemberOauths.get(0);
            UmsMember umsMember = memberMapper.selectByPrimaryKey(umsMemberOauth.getMemberId());
            UserDetails userDetails = userDetailsService.loadUserByUsername(umsMemberOauth.getOpenId());
            String token = jwtTokenUtil.generateToken(userDetails);
            result.setToken(token);
            result.setMemberId(umsMember.getId());
            boolean isPerfect = StrUtil.isEmpty(umsMember.getPhone());
            result.setIsPerfect(isPerfect);

            insertLoginLog(umsMember.getUsername(), sourceType);

            //修改用户统计信息
            //不存在则新增
            UmsMemberStatisticsInfoExample examples = new UmsMemberStatisticsInfoExample();
            examples.createCriteria().andMemberIdEqualTo(umsMember.getId());
            List<UmsMemberStatisticsInfo> umsMemberStatisticsInfos = memberStatisticsInfoMapper.selectByExample(examples);
            if(umsMemberStatisticsInfos.isEmpty())
            {
                UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
                umsMemberStatisticsInfo.setMemberId(umsMember.getId());
                umsMemberStatisticsInfo.setConsumeAmount(new BigDecimal(0));
                umsMemberStatisticsInfo.setOrderCount(0);
                umsMemberStatisticsInfo.setCouponCount(0);
                umsMemberStatisticsInfo.setCommentCount(0);
                umsMemberStatisticsInfo.setReturnOrderCount(0);
                umsMemberStatisticsInfo.setLoginCount(1);
                umsMemberStatisticsInfo.setAttendCount(0);
                umsMemberStatisticsInfo.setFansCount(0);
                umsMemberStatisticsInfo.setCollectProductCount(0);
                umsMemberStatisticsInfo.setCollectSubjectCount(0);
                umsMemberStatisticsInfo.setCollectTopicCount(0);
                umsMemberStatisticsInfo.setCollectCommentCount(0);
                umsMemberStatisticsInfo.setInviteFriendCount(0);
                memberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
            }else
            {
                UmsMemberStatisticsInfo umsMemberStatisticsInfo = umsMemberStatisticsInfos.get(0);
                umsMemberStatisticsInfo.setLoginCount(umsMemberStatisticsInfo.getLoginCount() + 1);
                memberStatisticsInfoMapper.updateByPrimaryKeySelective(umsMemberStatisticsInfo);
            }

            return CommonResult.success(result);
        }

        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(userInfo.getOpenId());
        umsMember.setNickname(userInfo.getNickName());
        umsMember.setGender(Convert.toInt(userInfo.getGender()));
        umsMember.setCity(userInfo.getCity());
        umsMember.setIcon(userInfo.getAvatarUrl());
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        umsMember.setSourceType(sourceType);

        //获取默认会员等级并设置
        UmsMemberLevelExample levelExample = new UmsMemberLevelExample();
        levelExample.createCriteria().andDefaultStatusEqualTo(1);
        List<UmsMemberLevel> memberLevelList = memberLevelMapper.selectByExample(levelExample);
        if (!CollectionUtils.isEmpty(memberLevelList)) {
            umsMember.setMemberLevelId(memberLevelList.get(0).getId());
        }

        memberMapper.insert(umsMember);

        //添加到用户授权表
        UmsMemberOauth umsMemberOauth = new UmsMemberOauth();
        umsMemberOauth.setMemberId(umsMember.getId());
        umsMemberOauth.setOpenId(userInfo.getOpenId());
        umsMemberOauth.setServer(server);
        memberOauthMapper.insert(umsMemberOauth);

        //登录成功，生成token返回
        UserDetails userDetails = userDetailsService.loadUserByUsername(umsMemberOauth.getOpenId());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateToken(userDetails);
        result.setToken(token);
        result.setMemberId(umsMember.getId());
        result.setIsPerfect(true);
        umsMember.setPassword(null);

        //修改用户统计信息
        //不存在则新增
        UmsMemberStatisticsInfoExample examples = new UmsMemberStatisticsInfoExample();
        examples.createCriteria().andMemberIdEqualTo(umsMember.getId());
        List<UmsMemberStatisticsInfo> umsMemberStatisticsInfos = memberStatisticsInfoMapper.selectByExample(examples);
        if(umsMemberStatisticsInfos.isEmpty())
        {
            UmsMemberStatisticsInfo umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
            umsMemberStatisticsInfo.setMemberId(umsMember.getId());
            umsMemberStatisticsInfo.setConsumeAmount(new BigDecimal(0));
            umsMemberStatisticsInfo.setOrderCount(0);
            umsMemberStatisticsInfo.setCouponCount(0);
            umsMemberStatisticsInfo.setCommentCount(0);
            umsMemberStatisticsInfo.setReturnOrderCount(0);
            umsMemberStatisticsInfo.setLoginCount(1);
            umsMemberStatisticsInfo.setAttendCount(0);
            umsMemberStatisticsInfo.setFansCount(0);
            umsMemberStatisticsInfo.setCollectProductCount(0);
            umsMemberStatisticsInfo.setCollectSubjectCount(0);
            umsMemberStatisticsInfo.setCollectTopicCount(0);
            umsMemberStatisticsInfo.setCollectCommentCount(0);
            umsMemberStatisticsInfo.setInviteFriendCount(0);
            memberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
        }else
        {
            UmsMemberStatisticsInfo umsMemberStatisticsInfo = umsMemberStatisticsInfos.get(0);
            umsMemberStatisticsInfo.setLoginCount(umsMemberStatisticsInfo.getLoginCount() + 1);
            memberStatisticsInfoMapper.updateByPrimaryKeySelective(umsMemberStatisticsInfo);
        }

        //记录用户登录日志
        insertLoginLog(umsMember.getUsername(), sourceType);

        return CommonResult.success(result, "注册成功");
    }

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }

        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);

        return CommonResult.success(sb.toString(), "获取验证码成功");
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(telephone);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(memberList)) {
            return CommonResult.failed("该账号不存在");
        }
        //验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
            return CommonResult.failed("验证码错误");
        }
        UmsMember umsMember = memberList.get(0);
        umsMember.setPassword(passwordEncoder.encode(password));
        memberMapper.updateByPrimaryKeySelective(umsMember);
        return CommonResult.success(null, "密码修改成功");
    }

    @Override
    public UmsMember getCurrentMember() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
        return memberDetails.getUmsMember();
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember record = new UmsMember();
        record.setId(id);
        record.setIntegration(integration);
        memberMapper.updateByPrimaryKeySelective(record);
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }

        //TODO:未对接验证码发送平台，测试用，上线要删除
        if(authCode.equals("123456"))
        {
            return true;
        }

        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        return authCode.equals(realAuthCode);
    }

    /**
     * 添加登录记录
     *
     * @param username 用户名
     */
    private void insertLoginLog(String username, int loginType) {
        UmsMember member = getByUsername(username);
        UmsMemberLoginLog loginLog = new UmsMemberLoginLog();
        loginLog.setId(member.getId());
        loginLog.setCreateTime(new Date());
        loginLog.setLoginType(loginType);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);
    }
}
