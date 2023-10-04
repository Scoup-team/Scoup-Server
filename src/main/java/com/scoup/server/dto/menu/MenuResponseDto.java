package com.scoup.server.dto.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.Event;
import com.scoup.server.domain.Menu;
import com.scoup.server.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class MenuResponseDto {
    //스탬프 정보랑 메뉴 정보 전달
    private Long id;
    private Long cafeId;
    private String cardName;
    private String cardNum;

    private String cafeName;
    private String cafeLocation;
    private String cafeImageUrl;
    private String cafeLicenseeNumber;
    private String cafePhoneNumber;
    private String masterUser;

    @Builder.Default
    private List<MenuDto> menu=new ArrayList<>();
}
