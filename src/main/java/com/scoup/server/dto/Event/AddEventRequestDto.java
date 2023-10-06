package com.scoup.server.dto.Event;

import lombok.Getter;

@Getter
public class AddEventRequestDto {
    private Long cafeId;
    private String content;
}
