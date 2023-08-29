package com.macro.mall.portal.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 售后订单查询参数
 */
@Getter
@Setter
public class OmsOrderReturnApplyListParam {
    @ApiModelProperty(value = "售后单状态：-1->全部，0->待处理；1->退货中；2->已完成；3->已拒绝")
    private Integer status;

    @ApiModelProperty(value = "分页模型")
    private PageParam page;
}
