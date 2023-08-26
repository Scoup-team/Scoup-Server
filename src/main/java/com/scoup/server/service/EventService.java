package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundException;
import com.scoup.server.domain.Event;
import com.scoup.server.domain.User;
import com.scoup.server.dto.Event.UpdateEventRequestDto;
import com.scoup.server.repository.EventRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public void patchEvent(Long eventId, UpdateEventRequestDto requestDto){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_EVENT_EXCEPTION));

        event.updateEvent(requestDto);

    }

    public void deleteEvent(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_EVENT_EXCEPTION));

        eventRepository.deleteById(event.getId());
    }
}
