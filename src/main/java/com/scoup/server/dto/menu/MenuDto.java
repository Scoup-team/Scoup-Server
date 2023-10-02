package com.scoup.server.dto.menu;

import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.UserOrder;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
public class MenuDto {
    private Long id;
    private String name;
    private Integer price;
    private Integer num;

    public void addNum(){
        this.num++;
    }
}
