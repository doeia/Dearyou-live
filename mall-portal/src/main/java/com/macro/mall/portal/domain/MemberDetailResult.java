package com.macro.mall.portal.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员中心信息
 */
@Data public class MemberDetailResult implements Serializable {

    private String nickName;

    private String avatarUrl;

    private String phone;

    private String idCard;
}
