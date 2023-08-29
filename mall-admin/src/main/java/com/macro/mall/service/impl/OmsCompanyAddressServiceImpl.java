package com.macro.mall.service.impl;

import com.macro.mall.mapper.OmsCompanyAddressMapper;
import com.macro.mall.model.OmsCompanyAddress;
import com.macro.mall.model.OmsCompanyAddressExample;
import com.macro.mall.service.OmsCompanyAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址管理Service实现类
 * Created by macro on 2018/10/18.
 */
@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {

    private final static Logger log = LoggerFactory.getLogger(OmsCompanyAddressServiceImpl.class);

    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;
    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }

    @Override
    public OmsCompanyAddress getItem(Long id){
        return companyAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(OmsCompanyAddress companyAddress){
        companyAddress.setId(null);

        return companyAddressMapper.insert(companyAddress);
    }

    @Override
    public int update(Long id, OmsCompanyAddress companyAddress){
        if(id==null){
            return 0;
        }

        companyAddress.setId(id);

        return companyAddressMapper.updateByPrimaryKeySelective(companyAddress);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return companyAddressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        OmsCompanyAddressExample example = new OmsCompanyAddressExample();
        example.createCriteria().andIdIn(ids);
        return companyAddressMapper.deleteByExample(example);
    }
}
