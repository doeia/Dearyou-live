package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.UmsMemberKeywordsMapper;
import com.macro.mall.model.UmsMemberKeywords;
import com.macro.mall.model.UmsMemberKeywordsExample;
import com.macro.mall.service.UmsMemberKeywordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 会员与关键词表（用户搜索的关键词）
 *
 * @author taobao
 * @date Sun Nov 17 11:02:47 CST 2019
 */
@Service
public class UmsMemberKeywordsServiceImpl implements UmsMemberKeywordsService {

    private final static Logger log = LoggerFactory.getLogger(UmsMemberKeywordsServiceImpl.class);

    @Autowired
    private UmsMemberKeywordsMapper memberKeywordsMapper;

    @Override
    public List<UmsMemberKeywords> list(Long memberId, String keywords, Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        UmsMemberKeywordsExample example = new UmsMemberKeywordsExample();
        UmsMemberKeywordsExample.Criteria criteria = example.createCriteria();
		example.setOrderByClause("id desc");
        if(memberId!=null){
            criteria.andMemberIdEqualTo(memberId);
        }
        if(!StringUtils.isEmpty(keywords)){
            criteria.andKeywordsLike("%"+keywords+"%");
        }

        return memberKeywordsMapper.selectByExample(example);
    }

    @Override
    public UmsMemberKeywords getItem(Long id){
        return memberKeywordsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(UmsMemberKeywords memberKeywords){
        memberKeywords.setId(null);

        return memberKeywordsMapper.insert(memberKeywords);
    }

    @Override
    public int update(Long id, UmsMemberKeywords memberKeywords){
        if(id==null){
            return 0;
        }

        memberKeywords.setId(id);

        return memberKeywordsMapper.updateByPrimaryKey(memberKeywords);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return memberKeywordsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        UmsMemberKeywordsExample example = new UmsMemberKeywordsExample();
        example.createCriteria().andIdIn(ids);
        return memberKeywordsMapper.deleteByExample(example);
    }
}