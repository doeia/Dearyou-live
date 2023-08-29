package com.macro.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.dto.CmsRegionsNode;
import com.macro.mall.dto.CmsRegionsParam;
import com.macro.mall.mapper.CmsRegionsMapper;
import com.macro.mall.model.CmsRegions;
import com.macro.mall.model.CmsRegionsExample;
import com.macro.mall.service.CmsRegionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 区域表
 *
 * @author taobao
 */
@Service
public class CmsRegionsServiceImpl implements CmsRegionsService {

    private final static Logger log = LoggerFactory.getLogger(CmsRegionsServiceImpl.class);

    @Autowired
    private CmsRegionsMapper regionsMapper;

    @Override
    public List<CmsRegions> list(Integer pageSize, Integer pageNum){
        PageHelper.startPage(pageNum, pageSize);
        CmsRegionsExample example = new CmsRegionsExample();
        CmsRegionsExample.Criteria criteria = example.createCriteria();

        return regionsMapper.selectByExample(example);
    }

    @Override
    public List<CmsRegionsNode> treeList() {
        CmsRegionsExample example = new CmsRegionsExample();
        example.createCriteria().andDeleteFlagEqualTo((byte)0);
        List<CmsRegions> list = regionsMapper.selectByExample(example);
        List<CmsRegionsNode> result = list.stream().filter(regions -> regions.getParentId().equals(0L))
                .map(regions -> convert(regions,list)).collect(Collectors.toList());

        return result;
    }

    private CmsRegionsNode convert(CmsRegions regions, List<CmsRegions> regionsList){
        CmsRegionsNode node = new CmsRegionsNode();
        BeanUtils.copyProperties(regions, node);
        List<CmsRegionsNode> children = regionsList.stream()
                .filter(subRegions->subRegions.getParentId().equals(regions.getId()))
                .map(subRegions->convert(subRegions,regionsList)).collect(Collectors.toList());

        node.setChildren(children);

        return node;
    }

    @Override
    public CmsRegions getItem(Long id){
        return regionsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int create(CmsRegionsParam param, Long operatorId, String operator){
        CmsRegions cmsRegions = new CmsRegions();
        cmsRegions.setName(param.getName());
        cmsRegions.setSpell(param.getSpell());
        cmsRegions.setShortSpell(param.getShortSpell());
        cmsRegions.setVer(param.getVer());
        cmsRegions.setCreatedTime(new Date());
        cmsRegions.setUpdateTime(new Date());
        cmsRegions.setCreatorId(operatorId);
        cmsRegions.setCreator(operator);
        cmsRegions.setLastOperator("");
        cmsRegions.setLastOperatorId(0L);
        cmsRegions.setDeleteFlag((byte)0);
        cmsRegions.setDisplayOrder(param.getDisplayOrder());
        if(param.getParentId().equals(0L)){
            cmsRegions.setProvinceId(0L);
            cmsRegions.setCityId(0L);
            cmsRegions.setProvinceName("");
            cmsRegions.setCityName("");
            cmsRegions.setParentId(0L);
            cmsRegions.setLayer((byte)1);
        }else{
            CmsRegions regions = regionsMapper.selectByPrimaryKey(param.getParentId());
            if(regions==null || regions.getDeleteFlag().equals((byte)1) || regions.getLayer().equals((byte)3)){
                return 0;
            }
            cmsRegions.setParentId(param.getParentId());
            cmsRegions.setLayer((byte)(regions.getLayer()+1));
            if(regions.getLayer().equals((byte)1)){
                cmsRegions.setProvinceName(regions.getName());
                cmsRegions.setProvinceId(regions.getId());
                cmsRegions.setCityName("");
                cmsRegions.setCityId(0L);
            }else if(regions.getLayer().equals((byte)2)){
                cmsRegions.setProvinceId(regions.getProvinceId());
                cmsRegions.setCityId(regions.getId());
                cmsRegions.setProvinceName(regions.getProvinceName());
                cmsRegions.setCityName(regions.getName());
            }else{
                return 0;
            }
        }

        return regionsMapper.insert(cmsRegions);
    }

    @Override
    public int update(Long id, CmsRegionsParam param, Long operatorId, String operator){
        if(id==null){
            return 0;
        }

        CmsRegions cmsRegions = regionsMapper.selectByPrimaryKey(id);
        if(cmsRegions==null){
            return 0;
        }

        cmsRegions.setName(param.getName());
        cmsRegions.setSpell(param.getSpell());
        cmsRegions.setShortSpell(param.getShortSpell());
        cmsRegions.setVer(param.getVer());
        cmsRegions.setUpdateTime(new Date());
        cmsRegions.setLastOperator(operator);
        cmsRegions.setLastOperatorId(operatorId);
        cmsRegions.setDeleteFlag((byte)0);
        cmsRegions.setDisplayOrder(param.getDisplayOrder());

        if(cmsRegions.getLayer().equals((byte)3)){
            return regionsMapper.updateByPrimaryKeySelective(cmsRegions);
        }else if(cmsRegions.getLayer().equals((byte)2)){
            int ret = regionsMapper.updateByPrimaryKeySelective(cmsRegions);

            //TODO 所有属于此城市的地区的城市名需要同步修改
            CmsRegions regions = new CmsRegions();
            regions.setCityName(cmsRegions.getName());
            CmsRegionsExample example = new CmsRegionsExample();
            example.createCriteria().andCityIdEqualTo(cmsRegions.getId());
            regionsMapper.updateByExampleSelective(regions,example);

            return ret;
        }else if(cmsRegions.getLayer().equals((byte)1)){
            int ret = regionsMapper.updateByPrimaryKeySelective(cmsRegions);

            //TODO 所有属于此省份的城市及其下的地区所包含的省份信息需要同步修改
            CmsRegions regions = new CmsRegions();
            regions.setProvinceName(cmsRegions.getName());
            CmsRegionsExample example = new CmsRegionsExample();
            example.createCriteria().andProvinceIdEqualTo(cmsRegions.getId());
            regionsMapper.updateByExampleSelective(regions,example);

            return ret;
        }else{
            return 0;
        }
    }

    @Override
    public int drop(Long id, Long operatorId, String operator){
        if(id==null){
            return 0;
        }
        CmsRegions cmsRegions = regionsMapper.selectByPrimaryKey(id);
        if(cmsRegions==null){
            return 0;
        }
        if(cmsRegions.getDeleteFlag().equals((byte)1)){
            return 1;
        }
        CmsRegionsExample example = new CmsRegionsExample();
        example.createCriteria().andParentIdEqualTo(cmsRegions.getId());
        long count = regionsMapper.countByExample(example);
        if(count>0){
            return 0;
        }

        cmsRegions.setUpdateTime(new Date());
        cmsRegions.setLastOperator(operator);
        cmsRegions.setLastOperatorId(operatorId);
        cmsRegions.setDeleteFlag((byte)1);

        return regionsMapper.updateByPrimaryKey(cmsRegions);
    }

}