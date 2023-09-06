package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.Event;
import com.scoup.server.domain.User;
import com.scoup.server.dto.cafe.SearchCafeResponseDto;
import com.scoup.server.dto.Event.EventResponseDto;
import com.scoup.server.dto.mainPage.UpdateMainPageRequestDto;
import com.scoup.server.repository.CafeRepository;
import com.scoup.server.repository.EventRepository;
import com.scoup.server.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class CafeService {

    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final EventRepository eventRepository;

    public List<SearchCafeResponseDto> searchCafe(Long userId, String keyword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        List<Cafe> cafeList = cafeRepository.findAllCafeContainingKeyword(keyword);

        return cafeList.stream()
            .map(SearchCafeResponseDto::of)
            .collect(Collectors.toList());
    }

    public void addCafe(Long userId, Long shopId) {
        User user=userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        if(user.getCafeIdList().contains(shopId)){
            throw new NotFoundException(ErrorMessage.REDUPLICATION_CAFE_EXCEPTION);
        }else{
            user.getCafeIdList().add(shopId);
        }
    }

    @Transactional
    public void deleteCafe(Long cafeId) {
        Cafe cafe=cafeRepository.findById(cafeId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

        cafeRepository.deleteById(cafe.getId());
    }

    public List<EventResponseDto> getEvent(Long cafeId){
        Cafe cafe=this.cafeRepository.findById(cafeId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

        List<Event> eventList=cafe.getEventList();
        List<EventResponseDto> dtoList=new ArrayList<>();


        for(int i=0; i<eventList.size(); i++){
            EventResponseDto tmp=EventResponseDto.builder()
                    .eventId(eventList.get(i).getId())
                    .content(eventList.get(i).getContent())
                    .createdAt(eventList.get(i).getCreatedAt())
                    .build();
            dtoList.add(tmp);
        }

        return dtoList;
    }

    public void addEvent(Long cafeId, String content){
        Cafe cafe=this.cafeRepository.findById(cafeId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

        Event event=Event.builder()
                .createdAt(LocalDateTime.now())
                .content(content)
                .cafe(cafe)
                .build();

        eventRepository.save(event);
        cafe.getEventList().add(event);
    }

    public void patchCafe(Long userId, Long cafeId){
        User user=userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        //카페 아이디 리스트 뽑아와서 있으면 삭제 없으면 추가
        if(user.getCafeIdList().contains(cafeId)){
            user.getCafeIdList().remove(cafeId);
        }else{
            user.getCafeIdList().add(cafeId);
        }

        userRepository.save(user);
    }
}
