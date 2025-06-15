package com.mygo.bangmall.search.controller;


import com.mygo.bangmall.search.service.ProductSaveService;
import com.mygo.common.exception.BaziCodeEnume;
import com.mygo.common.to.es.SkuEsModel;
import com.mygo.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search/save")
@RestController
@Slf4j
public class ElasticSaveController {
    @Autowired
    ProductSaveService productSaveService;
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean b = false;

        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (Exception e) {
            log.error("ElasticSaveController商品上架错误： {}",e );
            return R.error(BaziCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BaziCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (!b) {
            return R.ok();
        } else {
            return R.error(BaziCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BaziCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }

    }
}
