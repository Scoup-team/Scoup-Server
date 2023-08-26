package com.scoup.server.dto.menu;

import com.scoup.server.domain.Menu;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class MenuResponseDto {
    @Builder.Default
    private List<String> menu=new ArrayList<>();
}
