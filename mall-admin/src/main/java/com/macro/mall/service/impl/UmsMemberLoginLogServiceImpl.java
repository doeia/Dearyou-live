package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.UmsMemberLoginLogMapper;
import com.macro.mall.model.UmsMemberLoginLog;
import com.macro.mall.model.UmsMemberLoginLogExample;
import com.macro.mall.service.UmsMemberLoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员登录记录
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
@Service
public class UmsMemberLoginLogServiceImpl implements UmsMemberLoginLogService {

    private final static Logger log = LoggerFactory.getLogger(UmsMemberLoginLogServiceImpl.class);

    @Autowired
    private UmsMemberLoginLogMapper memberLoginLogMapper;

    @Override
    public List<UmsMemberLoginLog> list(Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        UmsMemberLoginLogExample example = new UmsMemberLoginLogExample();
        UmsMemberLoginLogExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id desc");

        return memberLoginLogMapper.selectByExample(example);
    }

    @Override
    public UmsMemberLoginLog getItem(Long id){
        return memberLoginLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(UmsMemberLoginLog memberLoginLog){
        memberLoginLog.setId(null);

        return memberLoginLogMapper.insert(memberLoginLog);
    }

    @Override
    public int update(Long id, UmsMemberLoginLog memberLoginLog){
        if(id==null){
            return 0;
        }

        memberLoginLog.setId(id);

        return memberLoginLogMapper.updateByPrimaryKey(memberLoginLog);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return memberLoginLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        UmsMemberLoginLogExample example = new UmsMemberLoginLogExample();
        example.createCriteria().andIdIn(ids);
        return memberLoginLogMapper.deleteByExample(example);
    }
}