package com.mygo.bangmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.bangmall.ware.vo.SkuHasStockVo;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:30:48
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

