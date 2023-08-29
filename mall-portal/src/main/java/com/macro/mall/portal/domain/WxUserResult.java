package com.macro.mall.portal.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信小程序获取用户信息传入的参数
 * Created by macro on 2018/8/30.
 */
@Data public class WxUserResult implements Serializable {

    private String token;

    private Long memberId;

    private Boolean isPerfect;
}
