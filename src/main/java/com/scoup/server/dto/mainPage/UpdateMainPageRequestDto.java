package com.scoup.server.dto.mainPage;

import com.scoup.server.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UpdateMainPageRequestDto {
    private Cafe cafe;
}
