package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundException;
import com.scoup.server.domain.Coupon;
import com.scoup.server.dto.coupon.CouponResponseDto;
import com.scoup.server.dto.coupon.UpdateCouponRequestDto;
import com.scoup.server.repository.CouponRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public CouponResponseDto getCoupon(Long userId){

        List<Coupon> couponList = couponRepository.findByUser_Id(userId);

        return CouponResponseDto.builder()
                .couponList(couponList)
                .build();
    }

    @Transactional
    public void patchCoupon(Long couponId, UpdateCouponRequestDto requestDto) {
        Coupon coupon=couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COUPON_EXCEPTION));

        coupon.updateCoupon(requestDto);
    }
}
