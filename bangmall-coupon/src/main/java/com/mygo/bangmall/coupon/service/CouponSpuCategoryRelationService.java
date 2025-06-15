package com.mygo.bangmall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.coupon.entity.CouponSpuCategoryRelationEntity;

import java.util.Map;

/**
 * 优惠券分类关联
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 19:56:37
 */
public interface CouponSpuCategoryRelationService extends IService<CouponSpuCategoryRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

