package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.OmsShipCompaniesMapper;
import com.macro.mall.model.OmsShipCompanies;
import com.macro.mall.model.OmsShipCompaniesExample;
import com.macro.mall.service.OmsShipCompaniesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配送公司表
 *
 * @author taobao
 */
@Service
public class OmsShipCompaniesServiceImpl implements OmsShipCompaniesService {

    private final static Logger log = LoggerFactory.getLogger(OmsShipCompaniesServiceImpl.class);

    @Autowired
    private OmsShipCompaniesMapper shipCompaniesMapper;

    @Override
    public List<OmsShipCompanies> list(Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        OmsShipCompaniesExample example = new OmsShipCompaniesExample();
        OmsShipCompaniesExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id desc");

        return shipCompaniesMapper.selectByExample(example);
    }

    @Override
    public OmsShipCompanies getItem(Long id){
        return shipCompaniesMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(OmsShipCompanies shipCompanies){
        shipCompanies.setId(null);

        return shipCompaniesMapper.insert(shipCompanies);
    }

    @Override
    public int update(Long id, OmsShipCompanies shipCompanies){
        if(id==null){
            return 0;
        }

        shipCompanies.setId(id);

        return shipCompaniesMapper.updateByPrimaryKeySelective(shipCompanies);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return shipCompaniesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        OmsShipCompaniesExample example = new OmsShipCompaniesExample();
        example.createCriteria().andIdIn(ids);
        return shipCompaniesMapper.deleteByExample(example);
    }
}