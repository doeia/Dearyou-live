package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.UmsAdminLoginLogMapper;
import com.macro.mall.model.UmsAdminLoginLog;
import com.macro.mall.model.UmsAdminLoginLogExample;
import com.macro.mall.service.UmsAdminLoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 后台用户登录日志表
 *
 * @author taobao
 * @date Sun Nov 17 16:44:14 CST 2019
 */
@Service
public class UmsAdminLoginLogServiceImpl implements UmsAdminLoginLogService {

    private final static Logger log = LoggerFactory.getLogger(UmsAdminLoginLogServiceImpl.class);

    @Autowired
    private UmsAdminLoginLogMapper adminLoginLogMapper;

    @Override
    public List<UmsAdminLoginLog> list(Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        UmsAdminLoginLogExample example = new UmsAdminLoginLogExample();
        UmsAdminLoginLogExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id desc");

        return adminLoginLogMapper.selectByExample(example);
    }

    @Override
    public UmsAdminLoginLog getItem(Long id){
        return adminLoginLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(UmsAdminLoginLog adminLoginLog){
        adminLoginLog.setId(null);

        return adminLoginLogMapper.insert(adminLoginLog);
    }

    @Override
    public int update(Long id, UmsAdminLoginLog adminLoginLog){
        if(id==null){
            return 0;
        }

        adminLoginLog.setId(id);

        return adminLoginLogMapper.updateByPrimaryKeySelective(adminLoginLog);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return adminLoginLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        UmsAdminLoginLogExample example = new UmsAdminLoginLogExample();
        example.createCriteria().andIdIn(ids);
        return adminLoginLogMapper.deleteByExample(example);
    }
}