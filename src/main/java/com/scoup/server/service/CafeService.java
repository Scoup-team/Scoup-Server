package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundException;
import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.Event;
import com.scoup.server.domain.User;
import com.scoup.server.dto.Event.EventResponseDto;
import com.scoup.server.dto.cafe.SearchCafeRequestDto;
import com.scoup.server.dto.cafe.SearchCafeResponseDto;
import com.scoup.server.dto.mainPage.UpdateMainPageRequestDto;
import com.scoup.server.repository.CafeRepository;
import com.scoup.server.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    public List<SearchCafeResponseDto> searchCafe(Long userId, String keyword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        List<Cafe> cafeList = cafeRepository.findAllCafeContainingKeyword(keyword);
        return cafeList.stream()
            .map(SearchCafeResponseDto::of)
            .collect(Collectors.toList());
    }

    public void addCafe(Long userId, SearchCafeRequestDto requestDto) {
        User user=userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        user.getCafeIdList().add(requestDto.getShopId());
    }

    @Transactional
    public void deleteCafe(Long cafeId) {
        Cafe cafe=cafeRepository.findById(cafeId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

        cafeRepository.deleteById(cafe.getId());
    }

    public EventResponseDto getEvent(Long cafeId){
        Cafe cafe=this.cafeRepository.findById(cafeId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

        List<Event> eventList=cafe.getEventList();

        return EventResponseDto.builder()
            .eventList(eventList)
            .build();
    }

    public void addEvent(Long eventId, Long cafeId, String content){
        Cafe cafe=this.cafeRepository.findById(cafeId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

        Event event=Event.builder()
            .createdAt(LocalDateTime.now())
            .content(content)
            .build();


        cafe.getEventList().add(event);
        //cafeRepository.save(cafe);
    }

    public void patchCafe(Long userId, UpdateMainPageRequestDto dto){
        User user=userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        Long cafeId=dto.getCafe().getId();

        //카페 아이디 리스트 뽑아와서 있으면 삭제 없으면 추가
        if(user.getCafeIdList().contains(cafeId)){
            user.getCafeIdList().remove(cafeId);
        }else{
            user.getCafeIdList().add(cafeId);
        }

        userRepository.save(user);
    }
}
