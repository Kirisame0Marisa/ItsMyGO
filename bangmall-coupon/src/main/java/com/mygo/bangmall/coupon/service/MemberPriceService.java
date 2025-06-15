package com.mygo.bangmall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 19:56:36
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

