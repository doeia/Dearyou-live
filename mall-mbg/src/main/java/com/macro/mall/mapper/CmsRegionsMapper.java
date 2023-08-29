package com.macro.mall.mapper;

import com.macro.mall.model.CmsRegions;
import com.macro.mall.model.CmsRegionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsRegionsMapper {
    long countByExample(CmsRegionsExample example);

    int deleteByExample(CmsRegionsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsRegions record);

    int insertSelective(CmsRegions record);

    List<CmsRegions> selectByExample(CmsRegionsExample example);

    CmsRegions selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsRegions record, @Param("example") CmsRegionsExample example);

    int updateByExample(@Param("record") CmsRegions record, @Param("example") CmsRegionsExample example);

    int updateByPrimaryKeySelective(CmsRegions record);

    int updateByPrimaryKey(CmsRegions record);
}