package com.macro.mall.portal.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 微信小程序获取用户信息传入的参数
 * Created by macro on 2018/8/30.
 */
@Data public class WxUserParam implements Serializable {

    private String sessionKey;

    private String encryptedData;

    private String iv;

    private String signature;

    private String rawData;
}
