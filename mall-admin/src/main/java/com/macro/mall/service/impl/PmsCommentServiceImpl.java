package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.dto.PmsCommentQueryParam;
import com.macro.mall.mapper.PmsCommentMapper;
import com.macro.mall.model.PmsComment;
import com.macro.mall.model.PmsCommentExample;
import com.macro.mall.service.PmsCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品评价表
 *
 * @author taobao
 */
@Service
public class PmsCommentServiceImpl implements PmsCommentService {

    private final static Logger log = LoggerFactory.getLogger(PmsCommentServiceImpl.class);

    @Autowired
    private PmsCommentMapper commentMapper;

    @Override
    public List<PmsComment> list(PmsCommentQueryParam queryParam, Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        PmsCommentExample example = new PmsCommentExample();
        PmsCommentExample.Criteria criteria = example.createCriteria();
        if(queryParam.getOrderId()!=null){
            criteria.andOrderIdEqualTo(queryParam.getOrderId());
        }
        if(!StringUtils.isEmpty(queryParam.getMemberNickName())){
            criteria.andMemberNickNameLike("%"+queryParam.getMemberNickName()+"%");
        }
        if(!StringUtils.isEmpty(queryParam.getProductName())){
            criteria.andProductNameLike("%"+queryParam.getProductName()+"%");
        }
        if(queryParam.getProductId()!=null){
            criteria.andProductIdEqualTo(queryParam.getProductId());
        }

        return commentMapper.selectByExample(example);
    }

    @Override
    public PmsComment getItem(Long id){
        return commentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(PmsComment comment){
        comment.setId(null);

        return commentMapper.insert(comment);
    }

    @Override
    public int update(Long id, PmsComment comment){
        if(id==null){
            return 0;
        }

        comment.setId(id);

        return commentMapper.updateByPrimaryKeySelective(comment);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        PmsCommentExample example = new PmsCommentExample();
        example.createCriteria().andIdIn(ids);
        return commentMapper.deleteByExample(example);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        if(id==null){
            return 0;
        }
        PmsComment comment = new PmsComment();
        comment.setId(id);
        comment.setShowStatus(status);
        return commentMapper.updateByPrimaryKeySelective(comment);
    }
}