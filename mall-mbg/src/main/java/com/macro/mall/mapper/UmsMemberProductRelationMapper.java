package com.macro.mall.mapper;

import com.macro.mall.model.UmsMemberProductRelation;
import com.macro.mall.model.UmsMemberProductRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberProductRelationMapper {
    long countByExample(UmsMemberProductRelationExample example);

    int deleteByExample(UmsMemberProductRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberProductRelation record);

    int insertSelective(UmsMemberProductRelation record);

    List<UmsMemberProductRelation> selectByExample(UmsMemberProductRelationExample example);

    UmsMemberProductRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsMemberProductRelation record, @Param("example") UmsMemberProductRelationExample example);

    int updateByExample(@Param("record") UmsMemberProductRelation record, @Param("example") UmsMemberProductRelationExample example);

    int updateByPrimaryKeySelective(UmsMemberProductRelation record);

    int updateByPrimaryKey(UmsMemberProductRelation record);
}