package com.mygo.bangmall.order.dao;

import com.mygo.bangmall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:18:24
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
