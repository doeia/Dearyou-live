package com.macro.mall.mapper;

import com.macro.mall.model.UmsMemberKeywords;
import com.macro.mall.model.UmsMemberKeywordsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberKeywordsMapper {
    long countByExample(UmsMemberKeywordsExample example);

    int deleteByExample(UmsMemberKeywordsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberKeywords record);

    int insertSelective(UmsMemberKeywords record);

    List<UmsMemberKeywords> selectByExample(UmsMemberKeywordsExample example);

    UmsMemberKeywords selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsMemberKeywords record, @Param("example") UmsMemberKeywordsExample example);

    int updateByExample(@Param("record") UmsMemberKeywords record, @Param("example") UmsMemberKeywordsExample example);

    int updateByPrimaryKeySelective(UmsMemberKeywords record);

    int updateByPrimaryKey(UmsMemberKeywords record);
}