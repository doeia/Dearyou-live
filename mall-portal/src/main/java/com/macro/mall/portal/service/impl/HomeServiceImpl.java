package com.macro.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.*;
import com.macro.mall.model.*;
import com.macro.mall.portal.domain.*;
import com.macro.mall.portal.service.HomeService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页内容管理Service实现类
 * Created by macro on 2019/1/28.
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
    @Autowired
    private SmsCouponMapper couponMapper;
    @Autowired
    private SmsArticleMapper articleMapper;
    @Autowired
    private PmsProductMapper productMapper;

    @Autowired
    private SmsHomeRecommendProductMapper homeRecommendProductMapper;

    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();
        //获取轮播列表
        result.setAdvertiseList(getHomeAdvertiseList());
        //获取轮播列表
        result.setAdvertise(getHomeAdvertise());
        //获取优惠券列表
        result.setCouponList(getCouponList());
        //获取文章列表
        result.setArticleList(getArticleList());
        //获取热销商品列表
        result.setHotProductList(getHotProductList());
        //获取dearYou商品列表
        result.setDearYouProductList(getDearYouProductList());
        //获取直播商品列表
        result.setLiveProductList(getLiveProductList());
        return result;
    }

    private List<AdvertiseDetailsResult> getHomeAdvertiseList() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andTypeEqualTo(1).andStatusEqualTo(1);
        example.setOrderByClause("sort desc");

        var list = advertiseMapper.selectByExample(example);

        List<AdvertiseDetailsResult> advertiseList = new ArrayList<>();

        for (var info : list)
        {
            AdvertiseDetailsResult advertiseDetailsResult = new AdvertiseDetailsResult();
            advertiseDetailsResult.setAdvertiseId(info.getId() != null ? info.getId() : 0);
            advertiseDetailsResult.setPic(info.getPic() != null ? info.getPic() : "");
            advertiseDetailsResult.setUrl(info.getUrl() != null ? info.getUrl() : "");

            advertiseList.add(advertiseDetailsResult);
        }

        return advertiseList;
    }

    private AdvertiseDetailsResult getHomeAdvertise() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andTypeEqualTo(0).andStatusEqualTo(1);

        List<SmsHomeAdvertise> list = advertiseMapper.selectByExample(example);

        AdvertiseDetailsResult advertiseDetailsResult = new AdvertiseDetailsResult();
        if(!list.isEmpty())
        {
            var info = list.get(0);
            advertiseDetailsResult.setAdvertiseId(info.getId() != null ? info.getId() : 0);
            advertiseDetailsResult.setPic(info.getPic() != null ? info.getPic() : "");
            advertiseDetailsResult.setUrl(info.getUrl() != null ? info.getUrl() : "");
        }
        return advertiseDetailsResult;
    }

    private List<CouponListResult> getCouponList() {
        PageHelper.startPage(1,4);

        SmsCouponExample example = new SmsCouponExample();
        example.createCriteria()
        .andEnableTimeLessThan(new Date())
        .andStartTimeLessThanOrEqualTo(new Date())
        .andEndTimeGreaterThanOrEqualTo(new Date());

        var list = couponMapper.selectByExample(example);

        List<CouponListResult> couponList = new ArrayList<>();

        for (var info : list)
        {
            CouponListResult couponListResult = new CouponListResult();
            couponListResult.setCouponId(info.getId() != null ? info.getId() : 0);
            couponListResult.setPic(info.getPic() != null ? info.getPic() : "");

            couponList.add(couponListResult);
        }

        return couponList;
    }

    private List<ArticleListResult>  getArticleList() {
        PageHelper.startPage(1,3);
        SmsArticleExample example = new SmsArticleExample();
        example.createCriteria()
        .andIsTopEqualTo(true);

        var list = articleMapper.selectByExample(example);

        List<ArticleListResult> articleList = new ArrayList<>();

        for (var info : list)
        {
            ArticleListResult articleListResult = new ArticleListResult();
            articleListResult.setArticleId(info.getId() != null ? info.getId() : 0);
            articleListResult.setPic(info.getPic() != null ? info.getPic() : "");
            articleListResult.setSynopsis(info.getSynopsis() != null ? info.getSynopsis() : "");
            articleListResult.setTitle(info.getTitle() != null ? info.getTitle() : "");

            articleList.add(articleListResult);
        }
        return articleList;
    }

    private List<ProductListResult> getHotProductList()
    {

        PageHelper.startPage(1,10);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria()
        .andDeleteStatusEqualTo(0)
        .andPublishStatusEqualTo(1)
        .andVerifyStatusEqualTo(1);
        example.setOrderByClause("sale desc");

        var list = productMapper.selectByExample(example);

        var recommendProductList = homeRecommendProductMapper.selectByExample(new SmsHomeRecommendProductExample());

        List<Long> pIdList = new ArrayList<>();

        for (var info : recommendProductList)
        {
            if(info.getRecommendStatus() == 1)
            {
                pIdList.add(info.getProductId());
            }
        }

        List<ProductListResult> productList = new ArrayList<>();

        for (var info : list)
        {
            if(pIdList.contains(info.getId()))
            {
                ProductListResult productListResult = new ProductListResult();
                productListResult.setProductId(info.getId() != null ? info.getId() : 0);
                productListResult.setPic(info.getPic() != null ? info.getPic() : "");
                productListResult.setName(info.getName() != null ? info.getName() : "");
                productListResult.setSubTitle(info.getSubTitle() != null ? info.getSubTitle() : "");
                productListResult.setPrice(info.getPrice() != null ? info.getPrice() : new BigDecimal(0));

                productList.add(productListResult);
            }
        }

        return productList;
    }

    private List<ProductListResult> getDearYouProductList()
    {
        PageHelper.startPage(1,3);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andPublishStatusEqualTo(1)
                .andVerifyStatusEqualTo(1)
                .andNewStatusEqualTo(1);
        example.setOrderByClause("sale desc");

        var list = productMapper.selectByExample(example);

        List<ProductListResult> productList = new ArrayList<>();

        for (var info : list)
        {
            ProductListResult productListResult = new ProductListResult();
            productListResult.setProductId(info.getId() != null ? info.getId() : 0);
            productListResult.setPic(info.getPic() != null ? info.getPic() : "");
            productListResult.setName(info.getName() != null ? info.getName() : "");
            productListResult.setSubTitle(info.getSubTitle() != null ? info.getSubTitle() : "");
            productListResult.setPrice(info.getPrice() != null ? info.getPrice() : new BigDecimal(0));

            productList.add(productListResult);
        }

        return productList;
    }

    private List<ProductListResult> getLiveProductList()
    {
        PageHelper.startPage(1,6);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andPublishStatusEqualTo(1)
                .andVerifyStatusEqualTo(1)
                .andRecommandStatusEqualTo(1);
        example.setOrderByClause("sale desc");

        var list = productMapper.selectByExample(example);

        List<ProductListResult> productList = new ArrayList<>();

        for (var info : list)
        {
            ProductListResult productListResult = new ProductListResult();
            productListResult.setProductId(info.getId() != null ? info.getId() : 0);
            productListResult.setPic(info.getPic() != null ? info.getPic() : "");
            productListResult.setName(info.getName() != null ? info.getName() : "");
            productListResult.setSubTitle(info.getSubTitle() != null ? info.getSubTitle() : "");
            productListResult.setPrice(info.getPrice() != null ? info.getPrice() : new BigDecimal(0));

            productList.add(productListResult);
        }

        return productList;
    }
}
