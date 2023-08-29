package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.dto.PmsCommentReplayQueryParam;
import com.macro.mall.mapper.PmsCommentReplayMapper;
import com.macro.mall.model.PmsCommentReplay;
import com.macro.mall.model.PmsCommentReplayExample;
import com.macro.mall.service.PmsCommentReplayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 产品评价回复表
 *
 * @author taobao
 */
@Service
public class PmsCommentReplayServiceImpl implements PmsCommentReplayService {

    private final static Logger log = LoggerFactory.getLogger(PmsCommentReplayServiceImpl.class);

    @Autowired
    private PmsCommentReplayMapper commentReplayMapper;

    @Override
    public List<PmsCommentReplay> list(PmsCommentReplayQueryParam queryParam, Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        PmsCommentReplayExample example = new PmsCommentReplayExample();
        PmsCommentReplayExample.Criteria criteria = example.createCriteria();
        if(queryParam.getCommentId()!=null){
            criteria.andCommentIdEqualTo(queryParam.getCommentId());
        }
        if(!StringUtils.isEmpty(queryParam.getMemberNickName())){
            criteria.andMemberNickNameLike("%"+queryParam.getMemberNickName()+"%");
        }
        if(queryParam.getType()!=null){
            criteria.andTypeEqualTo(queryParam.getType());
        }
        return commentReplayMapper.selectByExample(example);
    }

    @Override
    public PmsCommentReplay getItem(Long id){
        return commentReplayMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(PmsCommentReplay commentReplay){
        commentReplay.setId(null);

        return commentReplayMapper.insert(commentReplay);
    }

    @Override
    public int update(Long id, PmsCommentReplay commentReplay){
        if(id==null){
            return 0;
        }

        commentReplay.setId(id);

        return commentReplayMapper.updateByPrimaryKeySelective(commentReplay);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return commentReplayMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        PmsCommentReplayExample example = new PmsCommentReplayExample();
        example.createCriteria().andIdIn(ids);
        return commentReplayMapper.deleteByExample(example);
    }
}