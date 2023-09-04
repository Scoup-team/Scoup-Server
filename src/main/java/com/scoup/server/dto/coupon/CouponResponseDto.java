package com.scoup.server.dto.coupon;

import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.Coupon;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class CouponResponseDto {
    private Long couponId;
    private Integer period;
    private Boolean used;
    private String content;
    private LocalDateTime createdAt;
    private String shopName;
}
