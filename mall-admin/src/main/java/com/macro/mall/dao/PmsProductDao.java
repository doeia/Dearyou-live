package com.macro.mall.dao;

import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.model.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 商品自定义Dao
 * Created by macro on 2018/4/26.
 */
public interface PmsProductDao {
    /**
     * 获取商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);

    /**
     * 查询商品列表（过滤人气推荐或不推荐）
     * @param queryParam
     * @return
     */
    List<PmsProduct> getListForRecommend(@Param("queryParam")PmsProductQueryParam queryParam);
}
