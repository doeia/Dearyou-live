package com.macro.mall.portal.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员中心信息
 */
@Data public class MemberCenterResult implements Serializable {

    private String nickName;

    private String avatarUrl;

    private Long cartCount;
}
