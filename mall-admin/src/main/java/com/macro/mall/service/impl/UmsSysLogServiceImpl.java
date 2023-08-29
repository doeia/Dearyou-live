package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.UmsSysLogMapper;
import com.macro.mall.model.UmsSysLog;
import com.macro.mall.model.UmsSysLogExample;
import com.macro.mall.service.UmsSysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 日志管理Service实现
 */
@Service
public class UmsSysLogServiceImpl implements UmsSysLogService {

    private final static Logger log = LoggerFactory.getLogger(UmsSysLogServiceImpl.class);

    @Autowired
    private UmsSysLogMapper sysLogMapper;

    @Override
    public List<UmsSysLog> list(String admin, Integer deleted, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UmsSysLogExample example = new UmsSysLogExample();
        UmsSysLogExample.Criteria criteria = example.createCriteria();
		example.setOrderByClause("id desc");
        criteria.andDeletedEqualTo(deleted);
        if(!StringUtils.isEmpty(admin)) {
            criteria.andAdminLike("%" + admin + "%");
        }
        return sysLogMapper.selectByExample(example);
    }

    @Override
    public UmsSysLog getItem(Long id){
        return sysLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return sysLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        UmsSysLogExample example = new UmsSysLogExample();
        example.createCriteria().andIdIn(ids);
        return sysLogMapper.deleteByExample(example);
    }

    @Override
    public int drop(Long id) {
        if(id==null){
            return 0;
        }

        UmsSysLog sysLog = new UmsSysLog();
        sysLog.setId(id);
        sysLog.setDeleted(1);

        return sysLogMapper.updateByPrimaryKeySelective(sysLog);
    }

    @Override
    public int drop(List<Long> ids) {
        if(ids==null || ids.size()==0){
            return 0;
        }

        UmsSysLog sysLog = new UmsSysLog();
        sysLog.setDeleted(1);
        UmsSysLogExample example = new UmsSysLogExample();
        example.createCriteria().andIdIn(ids);
        return sysLogMapper.updateByExampleSelective(sysLog,example);
    }

    @Override
    public int recover(Long id) {
        if(id==null){
            return 0;
        }

        UmsSysLog sysLog = new UmsSysLog();
        sysLog.setId(id);
        sysLog.setDeleted(0);

        return sysLogMapper.updateByPrimaryKeySelective(sysLog);
    }

    @Override
    public int recover(List<Long> ids) {
        if(ids==null || ids.size()==0){
            return 0;
        }

        UmsSysLog sysLog = new UmsSysLog();
        sysLog.setDeleted(0);
        UmsSysLogExample example = new UmsSysLogExample();
        example.createCriteria().andIdIn(ids);
        return sysLogMapper.updateByExampleSelective(sysLog,example);
    }
}
