package com.mygo.bangmall.member.feign;

import com.mygo.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("bangmall-coupon")
public interface CouponFeignService {
    @RequestMapping("coupon/coupon/member/list")
    public R memberCouponList();


}
