package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.UmsMemberLevelMapper;
import com.macro.mall.model.UmsMemberLevel;
import com.macro.mall.model.UmsMemberLevelExample;
import com.macro.mall.service.UmsMemberLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员等级
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
@Service
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {

    private final static Logger log = LoggerFactory.getLogger(UmsMemberLevelServiceImpl.class);

    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;

    @Override
    public List<UmsMemberLevel> list(){
        UmsMemberLevelExample example = new UmsMemberLevelExample();
        UmsMemberLevelExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id desc");

        return memberLevelMapper.selectByExample(example);
    }

    @Override
    public UmsMemberLevel getItem(Long id){
        return memberLevelMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(UmsMemberLevel memberLevel){
        memberLevel.setId(null);

        return memberLevelMapper.insert(memberLevel);
    }

    @Override
    public int update(Long id, UmsMemberLevel memberLevel){
        if(id==null){
            return 0;
        }

        memberLevel.setId(id);

        return memberLevelMapper.updateByPrimaryKey(memberLevel);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return memberLevelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        UmsMemberLevelExample example = new UmsMemberLevelExample();
        example.createCriteria().andIdIn(ids);
        return memberLevelMapper.deleteByExample(example);
    }
}