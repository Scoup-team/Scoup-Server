package com.scoup.server.dto.menu;

import com.scoup.server.domain.Menu;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MenuResponseDto {
    private List<Menu> menuList=new ArrayList<>();
}
