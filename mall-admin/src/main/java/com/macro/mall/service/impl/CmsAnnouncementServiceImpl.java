package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.CmsAnnouncementMapper;
import com.macro.mall.model.CmsAnnouncement;
import com.macro.mall.model.CmsAnnouncementExample;
import com.macro.mall.service.CmsAnnouncementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 公告管理service实现类
 */
@Service
public class CmsAnnouncementServiceImpl implements CmsAnnouncementService {

    private final static Logger log = LoggerFactory.getLogger(CmsAnnouncementServiceImpl.class);

    @Autowired
    private CmsAnnouncementMapper announcementMapper;

    @Override
    public List<CmsAnnouncement> list(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        CmsAnnouncementExample example = new CmsAnnouncementExample();
        CmsAnnouncementExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id desc");

        return announcementMapper.selectByExample(example);
    }

    @Override
    public CmsAnnouncement getItem(Long id) {
        return announcementMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(CmsAnnouncement announcement) {
        announcement.setId(null);
        announcement.setCreateTime(new Date());
        return announcementMapper.insert(announcement);
    }

    @Override
    public int update(Long id, CmsAnnouncement announcement){
        if(id==null){
            return 0;
        }

        announcement.setId(id);
        announcement.setCreateTime(null);
        return announcementMapper.updateByPrimaryKeySelective(announcement);
    }

    @Override
    public int delete(Long id){
        if(id==null){
            return 0;
        }
        return announcementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids){
        if(ids==null || ids.size()==0){
            return 0;
        }

        CmsAnnouncementExample example = new CmsAnnouncementExample();
        example.createCriteria().andIdIn(ids);
        return announcementMapper.deleteByExample(example);
    }
}
