package com.mygo.bangmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mygo.bangmall.ware.MergeVo;
import com.mygo.bangmall.ware.vo.PurchaseDoneVo;
import com.mygo.common.utils.PageUtils;
import com.mygo.bangmall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author marisa
 * @email marisa@qq.com
 * @date 2024-10-16 20:30:48
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

