package com.mygo.bangmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:30:48
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

