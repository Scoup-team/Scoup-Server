package com.scoup.server.dto.menu;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReceiptRequestDto {
    private String store;
    private String cardName;
    private String cardNum;
    private List<MenuVO> items;
}
