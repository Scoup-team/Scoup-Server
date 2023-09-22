package com.scoup.server.dto.menu;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuVO {
    private String name;
    private Integer count;
    private Integer price;
}
