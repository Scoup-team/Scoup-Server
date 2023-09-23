package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.domain.Event;
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
    private final UserRepository userRepository;

    @Transactional
    public void patchEvent(Long eventId, UpdateEventRequestDto requestDto, Long adminUserId){

        User adminUser=userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        if(!adminUser.getMaster()){
            throw new NotFoundDataException(ErrorMessage.NOT_ADMIN_EXCEPTION);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_EVENT_EXCEPTION));

        event.updateEvent(requestDto);

    }

    public void deleteEvent(Long eventId, Long adminUserId){

        User adminUser=userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        if(!adminUser.getMaster()){
            throw new NotFoundDataException(ErrorMessage.NOT_ADMIN_EXCEPTION);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_EVENT_EXCEPTION));

        eventRepository.deleteById(event.getId());
    }
}
