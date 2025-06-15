package com.mygo.bangmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:18:24
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

