package com.macro.mall.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class UmsMemberOauth implements Serializable {
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "开放id")
    private byte[] openId;

    @ApiModelProperty(value = "服务商")
    private byte[] server;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public byte[] getOpenId() {
        return openId;
    }

    public void setOpenId(byte[] openId) {
        this.openId = openId;
    }

    public byte[] getServer() {
        return server;
    }

    public void setServer(byte[] server) {
        this.server = server;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", memberId=").append(memberId);
        sb.append(", openId=").append(openId);
        sb.append(", server=").append(server);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}