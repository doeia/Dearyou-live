package com.macro.mall.mapper;

import com.macro.mall.model.UmsMemberOauth;
import com.macro.mall.model.UmsMemberOauthExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberOauthMapper {
    long countByExample(UmsMemberOauthExample example);

    int deleteByExample(UmsMemberOauthExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberOauth record);

    int insertSelective(UmsMemberOauth record);

    List<UmsMemberOauth> selectByExample(UmsMemberOauthExample example);

    UmsMemberOauth selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsMemberOauth record, @Param("example") UmsMemberOauthExample example);

    int updateByExample(@Param("record") UmsMemberOauth record, @Param("example") UmsMemberOauthExample example);

    int updateByPrimaryKeySelective(UmsMemberOauth record);

    int updateByPrimaryKey(UmsMemberOauth record);
}