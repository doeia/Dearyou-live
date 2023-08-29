package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.SmsArticleMapper;
import com.macro.mall.model.SmsArticle;
import com.macro.mall.model.SmsArticleExample;
import com.macro.mall.service.SmsArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章表
 *
 * @author taobao
 * @date Sun Nov 17 16:44:14 CST 2019
 */
@Service
public class SmsArticleServiceImpl implements SmsArticleService {

    private final static Logger log = LoggerFactory.getLogger(SmsArticleServiceImpl.class);

    @Autowired
    private SmsArticleMapper articleMapper;

    @Override
    public List<SmsArticle> list(Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        SmsArticleExample example = new SmsArticleExample();
        SmsArticleExample.Criteria criteria = example.createCriteria();

        return articleMapper.selectByExample(example);
    }

    @Override
    public SmsArticle getItem(Long id){
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(SmsArticle article){
        article.setId(null);

        return articleMapper.insert(article);
    }

    @Override
    public int update(Long id, SmsArticle article){
        if(id==null){
            return 0;
        }

        article.setId(id);

        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        SmsArticleExample example = new SmsArticleExample();
        example.createCriteria().andIdIn(ids);
        return articleMapper.deleteByExample(example);
    }
}