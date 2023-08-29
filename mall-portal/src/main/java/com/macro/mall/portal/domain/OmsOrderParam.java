package com.macro.mall.portal.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单查询参数
 */
@Getter
@Setter
public class OmsOrderParam {
    @ApiModelProperty(value = "订单状态：-1->全部,0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;

    @ApiModelProperty(value = "分页模型")
    private PageParam page;
}
