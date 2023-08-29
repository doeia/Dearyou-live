package com.macro.mall.mapper;

import com.macro.mall.model.OmsShipCompanies;
import com.macro.mall.model.OmsShipCompaniesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OmsShipCompaniesMapper {
    long countByExample(OmsShipCompaniesExample example);

    int deleteByExample(OmsShipCompaniesExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OmsShipCompanies record);

    int insertSelective(OmsShipCompanies record);

    List<OmsShipCompanies> selectByExample(OmsShipCompaniesExample example);

    OmsShipCompanies selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OmsShipCompanies record, @Param("example") OmsShipCompaniesExample example);

    int updateByExample(@Param("record") OmsShipCompanies record, @Param("example") OmsShipCompaniesExample example);

    int updateByPrimaryKeySelective(OmsShipCompanies record);

    int updateByPrimaryKey(OmsShipCompanies record);
}