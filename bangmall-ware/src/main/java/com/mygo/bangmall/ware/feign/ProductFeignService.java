package com.mygo.bangmall.ware.feign;


import com.mygo.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("bangmall-product")
public interface ProductFeignService {
    @RequestMapping("product/skuinfo/info/{skuId}")
    // @RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId);
}
