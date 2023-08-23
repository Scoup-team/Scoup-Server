package com.scoup.server.dto.mainPage;

import com.scoup.server.domain.Cafe;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MainPageResponseDto {
    private List<Cafe> cafe;
}
