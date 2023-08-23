package com.scoup.server.dto.cafe;

import com.scoup.server.domain.Event;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CafeDto {
    private Long id;
    private String location;
    private String name;
    private String imageUrl;
    private List<Event> eventList;
}
