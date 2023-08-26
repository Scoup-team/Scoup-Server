package com.scoup.server.dto.cafe;

import com.scoup.server.domain.Event;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDto {
    private Long id;
    private String location;
    private String name;
    private String imageUrl;
    private List<Event> eventList;
}
