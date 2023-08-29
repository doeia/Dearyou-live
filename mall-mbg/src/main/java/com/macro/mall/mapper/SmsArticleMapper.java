package com.macro.mall.mapper;

import com.macro.mall.model.SmsArticle;
import com.macro.mall.model.SmsArticleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsArticleMapper {
    long countByExample(SmsArticleExample example);

    int deleteByExample(SmsArticleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsArticle record);

    int insertSelective(SmsArticle record);

    List<SmsArticle> selectByExampleWithBLOBs(SmsArticleExample example);

    List<SmsArticle> selectByExample(SmsArticleExample example);

    SmsArticle selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsArticle record, @Param("example") SmsArticleExample example);

    int updateByExampleWithBLOBs(@Param("record") SmsArticle record, @Param("example") SmsArticleExample example);

    int updateByExample(@Param("record") SmsArticle record, @Param("example") SmsArticleExample example);

    int updateByPrimaryKeySelective(SmsArticle record);

    int updateByPrimaryKeyWithBLOBs(SmsArticle record);

    int updateByPrimaryKey(SmsArticle record);
}