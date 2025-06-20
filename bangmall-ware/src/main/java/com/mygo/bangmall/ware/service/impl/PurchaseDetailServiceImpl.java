package com.mygo.bangmall.ware.service.impl;

import com.mygo.bangmall.ware.entity.PurchaseEntity;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mygo.common.utils.PageUtils;
import com.mygo.common.utils.Query;

import com.mygo.bangmall.ware.dao.PurchaseDetailDao;
import com.mygo.bangmall.ware.entity.PurchaseDetailEntity;
import com.mygo.bangmall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<PurchaseDetailEntity>();
        String key = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)) {
            queryWrapper.and(w->{
                w.eq("purchase_id", key).or().like("sku_id", key);
            });

        }
        String status = (String) params.get("status");
        if (!StringUtils.isNullOrEmpty(status)) {
            queryWrapper.eq("status", status);

        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isNullOrEmpty(key)) {
            queryWrapper.eq("ware_id", wareId);

        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetailByPurchaseId(Long id) {
       List<PurchaseDetailEntity> purchaseId =  this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id",id));
       return purchaseId;
    }

}