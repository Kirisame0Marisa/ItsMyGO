package com.mygo.bangmall.product.dao;

import com.mygo.bangmall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 15:33:07
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBathRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
