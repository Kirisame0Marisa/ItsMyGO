package com.mygo.bangmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:18:24
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

