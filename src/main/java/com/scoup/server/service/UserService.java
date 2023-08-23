package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundException;
import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.User;
import com.scoup.server.dto.mainPage.MainPageResponseDto;
import com.scoup.server.dto.user.UpdateUserPasswordRequestDto;
import com.scoup.server.dto.user.UpdateUserRequestDto;
import com.scoup.server.dto.user.UserDateResponseDto;
import com.scoup.server.repository.CafeRepository;
import com.scoup.server.repository.UserOrderRepository;
import com.scoup.server.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserOrderRepository userOrderRepository;
    private final CafeRepository cafeRepository;

    public UserDateResponseDto getUser(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        return UserDateResponseDto.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .build();
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void patchUser(Long userId, UpdateUserRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        user.updateUser(requestDto);
    }

    @Transactional
    public void patchUserPassword(Long userId, UpdateUserPasswordRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        user.updateUserPassword(requestDto);
    }

    public MainPageResponseDto findCafe(Long id){
        User user= userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));
        List<Long> cafeIdList=user.getCafeIdList();
        List<Cafe> cafeList=new ArrayList<>();

        for(int i=0; i<cafeIdList.size(); i++) {
            Cafe c = cafeRepository.findById(cafeIdList.get(i))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));
            cafeList.add(c);
        }

        return MainPageResponseDto.builder()
            .cafe(cafeList)
            .build();
    }

}
