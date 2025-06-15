package com.mygo.bangmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:30:48
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

