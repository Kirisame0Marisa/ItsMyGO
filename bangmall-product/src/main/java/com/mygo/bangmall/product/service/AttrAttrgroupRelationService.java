package com.mygo.bangmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.bangmall.product.vo.AttrGroupRelationVo;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 15:33:07
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBatch(List<AttrGroupRelationVo> vos);
}

