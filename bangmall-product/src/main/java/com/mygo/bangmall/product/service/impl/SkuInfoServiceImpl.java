package com.mygo.bangmall.product.service.impl;

import com.mygo.bangmall.product.config.MyThreadConfig;
import com.mygo.bangmall.product.entity.SkuImagesEntity;
import com.mygo.bangmall.product.entity.SpuInfoDescEntity;
import com.mygo.bangmall.product.service.SkuImagesService;
import com.mygo.bangmall.product.service.SkuSaleAttrValueService;
import com.mygo.bangmall.product.service.SpuInfoDescService;
import com.mygo.bangmall.product.vo.SkuItemSaleAttrVo;
import com.mygo.bangmall.product.vo.SkuItemVo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mygo.common.utils.PageUtils;
import com.mygo.common.utils.Query;

import com.mygo.bangmall.product.dao.SkuInfoDao;
import com.mygo.bangmall.product.entity.SkuInfoEntity;
import com.mygo.bangmall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SkuImagesService imagesService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isNullOrEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isNullOrEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            queryWrapper.eq("catalog_id", catelogId);
        }
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isNullOrEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            queryWrapper.eq("brand_id", brandId);
        }
        String min = (String) params.get("min");
        if(!StringUtils.isNullOrEmpty(min)){
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");
        if(!StringUtils.isNullOrEmpty(max)){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);
                if(bigDecimal.compareTo(new BigDecimal(0)) == 1){
                    queryWrapper.le("price", max);
                }
            }catch (Exception e){

            }

        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper

        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
       List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
       return list;
    }

    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity info = getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, executor);

        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSkuId());
            skuItemVo.setSaleAttr(saleAttrVos);

        }, executor);

        infoFuture.thenAcceptAsync((res) ->{
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
        }, executor);

        CompletableFuture.runAsync(() ->{
            //List<SkuImagesEntity> images = imagesService.getById(skuId);
        }, executor);

        CompletableFuture.allOf(saleAttrFuture).get();

        return skuItemVo;
    }

}