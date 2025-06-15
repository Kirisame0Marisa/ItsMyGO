package com.mygo.bangmall.order.dao;

import com.mygo.bangmall.order.entity.PaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付信息表
 * 
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:18:24
 */
@Mapper
public interface PaymentInfoDao extends BaseMapper<PaymentInfoEntity> {
	
}
