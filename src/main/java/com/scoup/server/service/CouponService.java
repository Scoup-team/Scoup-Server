package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.domain.Coupon;
import com.scoup.server.dto.coupon.CouponResponseDto;
import com.scoup.server.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResponseDto> getCoupon(Long userId){

        List<Coupon> couponList = couponRepository.findByUser_Id(userId);
        List<CouponResponseDto> dtoList=new ArrayList<>();

        for(int i=0; i<couponList.size(); i++){
            CouponResponseDto tmp=CouponResponseDto.builder()
                    .id(couponList.get(i).getId())
                    .period(couponList.get(i).getPeriod())
                    .used(couponList.get(i).getUsed())
                    .createdAt(couponList.get(i).getCreatedAt())
                    .shopName(couponList.get(i).getCafe().getName())
                    .build();
            dtoList.add(tmp);
        }

        return dtoList;
    }

    @Transactional
    public void patchCoupon(Long couponId) {
        Coupon coupon=couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_COUPON_EXCEPTION));

        coupon.updateCoupon();
    }
}
