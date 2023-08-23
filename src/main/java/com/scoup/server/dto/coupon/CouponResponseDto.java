package com.scoup.server.dto.coupon;

import com.scoup.server.domain.Coupon;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CouponResponseDto {
    private List<Coupon> couponList=new ArrayList<>();
}
