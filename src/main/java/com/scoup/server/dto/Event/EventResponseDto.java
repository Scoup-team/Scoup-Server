package com.scoup.server.dto.Event;

import com.scoup.server.domain.Event;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventResponseDto {
    private List<Event> eventList=new ArrayList<>();
}
