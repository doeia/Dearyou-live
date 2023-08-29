package com.macro.mall.service;

import com.macro.mall.dto.UmsMemberQueryParam;
import com.macro.mall.model.UmsMember;

import java.util.List;

/**
 * 会员管理Service
 */
public interface UmsMemberService {
    List<UmsMember> list(UmsMemberQueryParam param, Integer pageSize, Integer pageNum);
    UmsMember getItem(Long id);
    int create(UmsMember member);
    int update(Long id, UmsMember member);
    int updateStatus(List<Long> ids, Integer status);
}
