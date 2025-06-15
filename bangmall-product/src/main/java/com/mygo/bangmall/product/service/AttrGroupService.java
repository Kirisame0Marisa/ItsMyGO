package com.mygo.bangmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.bangmall.product.vo.AttrGroupwithAttrsVo;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 15:33:07
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
    PageUtils queryPage(Map<String, Object> params,Long catelogId);

    List<AttrGroupwithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

