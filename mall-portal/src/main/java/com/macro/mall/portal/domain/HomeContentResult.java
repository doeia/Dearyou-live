package com.macro.mall.portal.domain;

import com.macro.mall.model.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 首页内容返回信息封装
 * Created by macro on 2019/1/28.
 */
@Getter
@Setter
public class HomeContentResult
{
    @ApiModelProperty("轮播列表")
    private List<AdvertiseDetailsResult> advertiseList;

    @ApiModelProperty("优惠券列表")
    private List<CouponListResult> couponList;

    @ApiModelProperty("广告")
    private AdvertiseDetailsResult advertise;

    @ApiModelProperty("文章列表")
    private List<ArticleListResult> articleList;

    @ApiModelProperty("热销商品列表")
    private List<ProductListResult> hotProductList;

    @ApiModelProperty("dearYou商品列表")
    private List<ProductListResult> dearYouProductList;

    @ApiModelProperty("直播商品列表")
    private List<ProductListResult> liveProductList;
}
