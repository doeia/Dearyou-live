package com.macro.mall.mapper;

import com.macro.mall.model.PmsBrandCategory;
import com.macro.mall.model.PmsBrandCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsBrandCategoryMapper {
    long countByExample(PmsBrandCategoryExample example);

    int deleteByExample(PmsBrandCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsBrandCategory record);

    int insertSelective(PmsBrandCategory record);

    List<PmsBrandCategory> selectByExample(PmsBrandCategoryExample example);

    PmsBrandCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsBrandCategory record, @Param("example") PmsBrandCategoryExample example);

    int updateByExample(@Param("record") PmsBrandCategory record, @Param("example") PmsBrandCategoryExample example);

    int updateByPrimaryKeySelective(PmsBrandCategory record);

    int updateByPrimaryKey(PmsBrandCategory record);
}