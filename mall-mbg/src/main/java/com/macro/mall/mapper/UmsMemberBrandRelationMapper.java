package com.macro.mall.mapper;

import com.macro.mall.model.UmsMemberBrandRelation;
import com.macro.mall.model.UmsMemberBrandRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberBrandRelationMapper {
    long countByExample(UmsMemberBrandRelationExample example);

    int deleteByExample(UmsMemberBrandRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberBrandRelation record);

    int insertSelective(UmsMemberBrandRelation record);

    List<UmsMemberBrandRelation> selectByExample(UmsMemberBrandRelationExample example);

    UmsMemberBrandRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsMemberBrandRelation record, @Param("example") UmsMemberBrandRelationExample example);

    int updateByExample(@Param("record") UmsMemberBrandRelation record, @Param("example") UmsMemberBrandRelationExample example);

    int updateByPrimaryKeySelective(UmsMemberBrandRelation record);

    int updateByPrimaryKey(UmsMemberBrandRelation record);
}