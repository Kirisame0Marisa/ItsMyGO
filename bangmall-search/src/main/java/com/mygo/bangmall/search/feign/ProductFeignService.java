package com.mygo.bangmall.search.feign;

import com.mygo.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("bangmall-product")
public interface ProductFeignService {
    @GetMapping("/product/attr/info/{attrId}")
    // @RequiresPermissions("product:attr:info")
    public R attrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/brand/infos")
    public R brandsinfo(@RequestParam("brandIds") List<Long> brandIds);
}
