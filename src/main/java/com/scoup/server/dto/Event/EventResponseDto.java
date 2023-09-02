package com.scoup.server.dto.Event;

import com.scoup.server.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Getter
@Builder
//@AllArgsConstructor
public class EventResponseDto {
    private Long eventId;
    private String content;
    private LocalDateTime createdAt;

}
