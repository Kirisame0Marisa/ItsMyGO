package com.mygo.bangmall.product.vo;

import com.mygo.bangmall.product.entity.SkuInfoEntity;
import com.mygo.bangmall.product.entity.SkuSaleAttrValueEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {
    private SkuInfoEntity info;
    private List<SkuSaleAttrValueEntity> saleAttr;

    public void setSaleAttr(List<SkuItemSaleAttrVo> saleAttrVos) {
    }
}
