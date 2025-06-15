package com.mygo.bangmall.coupon.dao;

import com.mygo.bangmall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 19:56:36
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
