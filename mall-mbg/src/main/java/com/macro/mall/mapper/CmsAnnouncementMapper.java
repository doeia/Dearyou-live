package com.macro.mall.mapper;

import com.macro.mall.model.CmsAnnouncement;
import com.macro.mall.model.CmsAnnouncementExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsAnnouncementMapper {
    long countByExample(CmsAnnouncementExample example);

    int deleteByExample(CmsAnnouncementExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsAnnouncement record);

    int insertSelective(CmsAnnouncement record);

    List<CmsAnnouncement> selectByExampleWithBLOBs(CmsAnnouncementExample example);

    List<CmsAnnouncement> selectByExample(CmsAnnouncementExample example);

    CmsAnnouncement selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsAnnouncement record, @Param("example") CmsAnnouncementExample example);

    int updateByExampleWithBLOBs(@Param("record") CmsAnnouncement record, @Param("example") CmsAnnouncementExample example);

    int updateByExample(@Param("record") CmsAnnouncement record, @Param("example") CmsAnnouncementExample example);

    int updateByPrimaryKeySelective(CmsAnnouncement record);

    int updateByPrimaryKeyWithBLOBs(CmsAnnouncement record);

    int updateByPrimaryKey(CmsAnnouncement record);
}