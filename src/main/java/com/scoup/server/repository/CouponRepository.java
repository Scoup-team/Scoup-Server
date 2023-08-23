package com.scoup.server.repository;

import com.scoup.server.domain.Coupon;
import com.scoup.server.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUser_Id(Long id);
}
