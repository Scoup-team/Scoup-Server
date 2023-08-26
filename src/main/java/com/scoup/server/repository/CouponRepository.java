package com.scoup.server.repository;

import com.scoup.server.domain.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUser_Id(Long id);
}
