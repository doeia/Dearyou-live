package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.PmsFeightTemplateMapper;
import com.macro.mall.model.PmsFeightTemplate;
import com.macro.mall.model.PmsFeightTemplateExample;
import com.macro.mall.service.PmsFeightTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运费模版
 *
 * @author taobao
 */
@Service
public class PmsFeightTemplateServiceImpl implements PmsFeightTemplateService {

    private final static Logger log = LoggerFactory.getLogger(PmsFeightTemplateServiceImpl.class);

    @Autowired
    private PmsFeightTemplateMapper feightTemplateMapper;

    @Override
    public List<PmsFeightTemplate> list(Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        PmsFeightTemplateExample example = new PmsFeightTemplateExample();
        PmsFeightTemplateExample.Criteria criteria = example.createCriteria();

        return feightTemplateMapper.selectByExample(example);
    }

    @Override
    public PmsFeightTemplate getItem(Long id){
        return feightTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(PmsFeightTemplate feightTemplate){
        feightTemplate.setId(null);

        return feightTemplateMapper.insert(feightTemplate);
    }

    @Override
    public int update(Long id, PmsFeightTemplate feightTemplate){
        if(id==null){
            return 0;
        }

        feightTemplate.setId(id);

        return feightTemplateMapper.updateByPrimaryKeySelective(feightTemplate);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return feightTemplateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        PmsFeightTemplateExample example = new PmsFeightTemplateExample();
        example.createCriteria().andIdIn(ids);
        return feightTemplateMapper.deleteByExample(example);
    }
}