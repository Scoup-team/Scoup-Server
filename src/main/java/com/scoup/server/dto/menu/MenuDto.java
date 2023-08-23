package com.scoup.server.dto.menu;

import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.UserOrder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuDto {
    private Long id;
    private String name;
    private Integer price;
    private Cafe cafe;
    private List<UserOrder> userOrderList;
}
