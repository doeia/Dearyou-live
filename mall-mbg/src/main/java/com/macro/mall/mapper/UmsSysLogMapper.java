package com.macro.mall.mapper;

import com.macro.mall.model.UmsSysLog;
import com.macro.mall.model.UmsSysLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsSysLogMapper {
    long countByExample(UmsSysLogExample example);

    int deleteByExample(UmsSysLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsSysLog record);

    int insertSelective(UmsSysLog record);

    List<UmsSysLog> selectByExampleWithBLOBs(UmsSysLogExample example);

    List<UmsSysLog> selectByExample(UmsSysLogExample example);

    UmsSysLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsSysLog record, @Param("example") UmsSysLogExample example);

    int updateByExampleWithBLOBs(@Param("record") UmsSysLog record, @Param("example") UmsSysLogExample example);

    int updateByExample(@Param("record") UmsSysLog record, @Param("example") UmsSysLogExample example);

    int updateByPrimaryKeySelective(UmsSysLog record);

    int updateByPrimaryKeyWithBLOBs(UmsSysLog record);

    int updateByPrimaryKey(UmsSysLog record);
}