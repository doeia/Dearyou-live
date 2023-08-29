package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.PmsBrandCategoryMapper;
import com.macro.mall.model.PmsBrandCategory;
import com.macro.mall.model.PmsBrandCategoryExample;
import com.macro.mall.service.PmsBrandCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 品牌分类表
 *
 * @author taobao
 * @date Sat Nov 16 21:46:24 CST 2019
 */
@Service
public class PmsBrandCategoryServiceImpl implements PmsBrandCategoryService {

    private final static Logger log = LoggerFactory.getLogger(PmsBrandCategoryServiceImpl.class);

    @Autowired
    private PmsBrandCategoryMapper brandCategoryMapper;

    @Override
    public List<PmsBrandCategory> list(Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        PmsBrandCategoryExample example = new PmsBrandCategoryExample();
        PmsBrandCategoryExample.Criteria criteria = example.createCriteria();

        return brandCategoryMapper.selectByExample(example);
    }

    @Override
    public PmsBrandCategory getItem(Long id){
        return brandCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(PmsBrandCategory brandCategory){
        brandCategory.setId(null);

        return brandCategoryMapper.insert(brandCategory);
    }

    @Override
    public int update(Long id, PmsBrandCategory brandCategory){
        if(id==null){
            return 0;
        }

        brandCategory.setId(id);

        return brandCategoryMapper.updateByPrimaryKeySelective(brandCategory);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return brandCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        PmsBrandCategoryExample example = new PmsBrandCategoryExample();
        example.createCriteria().andIdIn(ids);
        return brandCategoryMapper.deleteByExample(example);
    }

    @Override
    public List<PmsBrandCategory> list() {
        PmsBrandCategoryExample example = new PmsBrandCategoryExample();
        return brandCategoryMapper.selectByExample(example);
    }
}