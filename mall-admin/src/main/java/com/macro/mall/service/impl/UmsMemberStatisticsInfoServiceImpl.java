package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.dao.UmsMemberStatisticsInfoDao;
import com.macro.mall.dto.UmsMemberStatisticsInfoNumberResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoSexResult;
import com.macro.mall.dto.UmsMemberStatisticsInfoTimeResult;
import com.macro.mall.mapper.UmsMemberStatisticsInfoMapper;
import com.macro.mall.model.UmsMemberStatisticsInfo;
import com.macro.mall.model.UmsMemberStatisticsInfoExample;
import com.macro.mall.service.UmsMemberStatisticsInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员统计信息
 *
 * @author taobao
 * @date Sun Nov 17 11:02:46 CST 2019
 */
@Service
public class UmsMemberStatisticsInfoServiceImpl implements UmsMemberStatisticsInfoService {

    private final static Logger log = LoggerFactory.getLogger(UmsMemberStatisticsInfoServiceImpl.class);

    @Autowired
    private UmsMemberStatisticsInfoMapper memberStatisticsInfoMapper;

    @Autowired
    private UmsMemberStatisticsInfoDao memberStatisticsInfoDao;

    @Override
    public List<UmsMemberStatisticsInfo> list(Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        UmsMemberStatisticsInfoExample example = new UmsMemberStatisticsInfoExample();
        UmsMemberStatisticsInfoExample.Criteria criteria = example.createCriteria();

        return memberStatisticsInfoMapper.selectByExample(example);
    }

    @Override
    public UmsMemberStatisticsInfo getItem(Long id){
        return memberStatisticsInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(UmsMemberStatisticsInfo memberStatisticsInfo){
        memberStatisticsInfo.setId(null);

        return memberStatisticsInfoMapper.insert(memberStatisticsInfo);
    }

    @Override
    public int update(Long id, UmsMemberStatisticsInfo memberStatisticsInfo){
        if(id==null){
            return 0;
        }

        memberStatisticsInfo.setId(id);

        return memberStatisticsInfoMapper.updateByPrimaryKey(memberStatisticsInfo);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return memberStatisticsInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        UmsMemberStatisticsInfoExample example = new UmsMemberStatisticsInfoExample();
        example.createCriteria().andIdIn(ids);
        return memberStatisticsInfoMapper.deleteByExample(example);
    }

    @Override
    public UmsMemberStatisticsInfo getStatInfo(Long memberId) {
        UmsMemberStatisticsInfo info = memberStatisticsInfoDao.getStatInfo(memberId);
        return info;
    }

    @Override
    public UmsMemberStatisticsInfoSexResult getStatInfoOfSex() {
        return memberStatisticsInfoDao.getStatInfoOfSex();
    }

    @Override
    public List<UmsMemberStatisticsInfoTimeResult> getStatInfoOfTime(Integer mode, String date) {
        List<UmsMemberStatisticsInfoTimeResult> list = memberStatisticsInfoDao.getStatInfoOfTime(mode,date);
        return list;
    }

    @Override
    public UmsMemberStatisticsInfoNumberResult getStatInfoOfNumber() {
        UmsMemberStatisticsInfoNumberResult result = memberStatisticsInfoDao.getStatInfoOfNumber();
        return result;
    }
}