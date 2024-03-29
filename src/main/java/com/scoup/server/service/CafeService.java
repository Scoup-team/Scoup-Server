package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.controller.exception.UserForbiddenException;
import com.scoup.server.domain.*;
import com.scoup.server.dto.Event.AddEventRequestDto;
import com.scoup.server.dto.Event.EventResponseDto;
import com.scoup.server.dto.admin.PatchAdminCafeRequestDto;
import com.scoup.server.dto.cafe.AdminCafeRequestDto;
import com.scoup.server.dto.cafe.SearchCafeResponseDto;
import com.scoup.server.dto.menu.ReceiptRequestDto;
import com.scoup.server.dto.menu.ReceiptResponseDto;
import com.scoup.server.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final UserOrderRepository userOrderRepository;
    private final MenuRepository menuRepository;
    private final StampRepository stampRepository;
    private final CouponRepository couponRepository;

    public List<SearchCafeResponseDto> searchCafe(Long userId, String keyword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        List<Cafe> cafeList = cafeRepository.findAllCafeContainingKeyword(keyword);

        return cafeList.stream()
            .map(SearchCafeResponseDto::of)
            .collect(Collectors.toList());
    }

    public void addCafe(Long userId, Long shopId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        if (user.getCafeIdList().contains(shopId)) {
            throw new NotFoundDataException(ErrorMessage.REDUPLICATION_CAFE_EXCEPTION);
        } else {
            user.getCafeIdList().add(shopId);
        }
    }

    @Transactional
    public void deleteCafe(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        List<Cafe> cafe=cafeRepository.findByUser_Id(userId);

        if(cafe.size() != 1){
            throw new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION);
        }

        cafeRepository.deleteById(cafe.get(0).getId());
    }

    public List<EventResponseDto> getEvent(Long cafeId) {
        Cafe cafe = this.cafeRepository.findById(cafeId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

        List<Event> eventList = cafe.getEventList();
        List<EventResponseDto> dtoList = new ArrayList<>();

        for (int i = 0; i < eventList.size(); i++) {
            EventResponseDto tmp = EventResponseDto.builder()
                .eventId(eventList.get(i).getId())
                .content(eventList.get(i).getContent())
                .createdAt(eventList.get(i).getCreatedAt())
                .build();
            dtoList.add(tmp);
        }

        return dtoList;
    }

    public void addEvent(AddEventRequestDto requestDto, Long adminUserId) {
        //Long cafeId=requestDto.getCafeId();
        String content=requestDto.getContent();

        User adminUser=userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        if(!adminUser.getMaster()){
            throw new NotFoundDataException(ErrorMessage.NOT_ADMIN_EXCEPTION);
        }

        List<Cafe> cafeList = this.cafeRepository.findByUser_Id(adminUserId);

        if(cafeList.isEmpty()){
            throw new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION);
        }

        if(cafeList.size()>1){
            //혹시 같아이디로 카페 하나 이상 만들어질까봐 넣음
            throw new NotFoundDataException(ErrorMessage.REDUPLICATION_CAFE_ADD_EXCEPTION);
        }

        Event event = Event.builder()
            .createdAt(LocalDateTime.now())
            .content(content)
            .cafe(cafeList.get(0))
            .build();

        eventRepository.save(event);
        cafeList.get(0).getEventList().add(event);
    }

    public void deleteMainPageCafe(Long userId, Long cafeId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        //카페 아이디 리스트 뽑아와서 있으면 삭제
        if (user.getCafeIdList().contains(cafeId)) {
            user.getCafeIdList().remove(cafeId);
        } else {
            throw new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION);
        }

        userRepository.save(user);
    }

    @Transactional
    public ReceiptResponseDto postReceipt(ReceiptRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));
        Cafe cafe = cafeRepository.findByName(requestDto.getStore())
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

        Stamp stamp = stampRepository.save(Stamp.builder()
            .user(user)
            .cafe(cafe)
            .cardName(requestDto.getCardName())
            .cardNum(requestDto.getCardNum())
            .build());

        requestDto.getItems().stream()
            .forEach(menu -> {
                Optional<Menu> menuData = menuRepository.findByName(menu.getName());

                if (menuData.isEmpty()) {
                    Menu saveMenu = menuRepository.save(Menu.builder()
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .imageUrl("123")
                        .cafe(cafe)
                        .build());
                    stampSave(saveMenu, user, stamp);
                } else {
                    stampSave(menuData.get(), user, stamp);
                }
            });

        //스탬프 12개면 쿠폰 하나 추가
        List<Stamp> tmpList=stampRepository.findByUser_Id(user.getId());
        List<Stamp> stampList=tmpList.stream().filter(a->a.getCafe()==cafe).collect(Collectors.toList());
        //스탬프 12개인지 확인
        if((stampList.size()%12)==0){
            createCoupon(user, "아이스 아메리카노 1잔", cafe);
        }


        return ReceiptResponseDto.builder()
            .store(cafe.getName())
            .cardNum(requestDto.getCardNum())
            .cardName(requestDto.getCardName())
            .items(requestDto.getItems())
            .stampId(stamp.getId())
            .build();
    }

    public void stampSave(Menu menu, User user, Stamp stamp) {
        userOrderRepository.save(UserOrder.builder()
            .user(user)
            .menu(menu)
            .stamp(stamp)
            .build());
    }

    public void createCoupon(User user, String content, Cafe cafe){
        //스탬프 12개 채우면 쿠폰 하나 추가
        couponRepository.save(Coupon.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .period(100)
                .used(false)
                .user(user)
                .cafe(cafe)
                .build()
        );

        //스탬프 삭제
        List<Stamp> tmplist=stampRepository.findByUser_Id(user.getId());

        for(int i=0; i<tmplist.size(); i++){
            if(tmplist.get(i).getCafe()==cafe){
                userOrderRepository.deleteAll(userOrderRepository.findByStamp_Id(tmplist.get(i).getId()));
                stampRepository.delete(tmplist.get(i));
            }
        }
    }

    @Transactional
    public void patchCafe(PatchAdminCafeRequestDto requestDto, Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        List<Cafe> cafe=cafeRepository.findByUser_Id(userId);

        if(cafe.size() != 1){
            throw new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION);
        }

        cafe.get(0).patchCafe(requestDto);
    }

    public void addAdminCafe(Long adminUserId, AdminCafeRequestDto adminCafeRequestDto){
        User adminUser=userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        List<Cafe> tmpCafeList=cafeRepository.findByUser_Id(adminUserId);

        if(!tmpCafeList.isEmpty()){
            throw new NotFoundDataException(ErrorMessage.REDUPLICATION_CAFE_ADD_EXCEPTION);
        }

        Cafe cafe=Cafe.builder()
                .name(adminCafeRequestDto.getShopName())
                .phoneNumber(adminCafeRequestDto.getPhoneNumber())
                .licenseeNumber(adminCafeRequestDto.getLicenseeNumber())
                .runningTime(adminCafeRequestDto.getRunningTime())
                .location(adminCafeRequestDto.getShopAddress())
                .imageUrl(adminCafeRequestDto.getShopImageUrl())
                .user(adminUser)
                .build();

        cafeRepository.save(cafe);
    }

    public List<EventResponseDto> getAdminEvent(Long userId) {

        User user=userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        if(!user.getMaster()){
            throw new UserForbiddenException(ErrorMessage.FORBIDDEN_USER_EXCEPTION);
        }

        List<Cafe> cafeList=this.cafeRepository.findByUser_Id(userId);

        if(cafeList.isEmpty()){
            throw new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION);
        }

        if(cafeList.size()>1){
            //혹시 같아이디로 카페 하나 이상 만들어질까봐 넣음
            throw new NotFoundDataException(ErrorMessage.REDUPLICATION_CAFE_ADD_EXCEPTION);
        }

        List<Event> eventList = cafeList.get(0).getEventList();
        List<EventResponseDto> dtoList = new ArrayList<>();

        for (int i = 0; i < eventList.size(); i++) {
            EventResponseDto tmp = EventResponseDto.builder()
                    .eventId(eventList.get(i).getId())
                    .content(eventList.get(i).getContent())
                    .createdAt(eventList.get(i).getCreatedAt())
                    .build();
            dtoList.add(tmp);
        }

        return dtoList;
    }
}
