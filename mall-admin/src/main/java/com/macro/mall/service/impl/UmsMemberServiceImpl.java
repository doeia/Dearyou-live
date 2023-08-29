package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.dto.UmsMemberQueryParam;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.model.UmsMember;
import com.macro.mall.model.UmsMemberExample;
import com.macro.mall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 会员管理Service实现类
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    private UmsMemberMapper memberMapper;

    @Override
    public List<UmsMember> list(UmsMemberQueryParam param, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsMemberExample example = new UmsMemberExample();
        UmsMemberExample.Criteria criteria = example.createCriteria();
		example.setOrderByClause("id desc");
        if(param.getStatus()!=null){
            criteria.andStatusEqualTo(param.getStatus());
        }
        if(!StringUtils.isEmpty(param.getNickname())){
            criteria.andNicknameLike("%"+param.getNickname()+"%");
        }
        return memberMapper.selectByExample(example);
    }

    @Override
    public UmsMember getItem(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(UmsMember member) {
        member.setId(null);
        member.setCreateTime(new Date());
        //TODO 会员密码加密机制未知，不予入库
        member.setPassword(null);
        return memberMapper.insert(member);
    }

    @Override
    public int update(Long id, UmsMember member) {
        member.setId(id);
        //用户名不可修改
        member.setUsername(null);
        //密码需加密处理
        member.setPassword(null);
        return memberMapper.updateByPrimaryKeySelective(member);
    }

    @Override
    public int updateStatus(List<Long> ids, Integer status) {
        UmsMember record = new UmsMember();
        record.setStatus(status);
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andIdIn(ids);
        return memberMapper.updateByExampleSelective(record, example);
    }
}
