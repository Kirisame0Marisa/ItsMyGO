package com.mygo.bangmall.coupon.service.impl;

import com.mygo.bangmall.coupon.entity.MemberPriceEntity;
import com.mygo.bangmall.coupon.entity.SkuLadderEntity;
import com.mygo.bangmall.coupon.service.MemberPriceService;
import com.mygo.bangmall.coupon.service.SkuLadderService;
import com.mygo.common.to.MemberPrice;
import com.mygo.common.to.SkuReductionTo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mygo.common.utils.PageUtils;
import com.mygo.common.utils.Query;

import com.mygo.bangmall.coupon.dao.SkuFullReductionDao;
import com.mygo.bangmall.coupon.entity.SkuFullReductionEntity;
import com.mygo.bangmall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;
    @Autowired
    MemberPriceService memberPriceService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo reductionTo) {
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(reductionTo.getSkuId());
        skuLadderEntity.setFullCount(reductionTo.getFullCount());
        skuLadderEntity.setDiscount(reductionTo.getDiscount());
        skuLadderEntity.setAddOther(reductionTo.getCountStatus());
        if(reductionTo.getFullCount()>0){
            skuLadderService.save(skuLadderEntity);
        }


        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(reductionTo, reductionEntity);
        if(reductionEntity.getFullPrice().compareTo(new BigDecimal("0")) == 1){
            this.save(reductionEntity);
        }


        List<MemberPrice> memberPrices = reductionTo.getMemberPrice();

        List<MemberPriceEntity> collect = memberPrices.stream().map(item->{
            MemberPriceEntity priceEntity = new MemberPriceEntity();
            priceEntity.setSkuId(reductionTo.getSkuId());
            priceEntity.setMemberLevelId(item.getId());
            priceEntity.setMemberLevelName(item.getName());
            priceEntity.setMemberPrice(item.getPrice());
            priceEntity.setAddOther(1);
            return priceEntity;
        }).filter(item->{
            return item.getMemberPrice().compareTo(new BigDecimal("0")) == 1;
                }).collect(Collectors.toList());

        memberPriceService.saveBatch(collect);


    }

}