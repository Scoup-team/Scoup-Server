package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.dto.mainPage.MainPageCafeResponseDto;
import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.User;
import com.scoup.server.domain.UserOrder;
import com.scoup.server.dto.user.UpdateUserPasswordRequestDto;
import com.scoup.server.dto.user.UpdateUserRequestDto;
import com.scoup.server.dto.user.UserDateResponseDto;
import com.scoup.server.repository.CafeRepository;
import com.scoup.server.repository.MenuRepository;
import com.scoup.server.repository.UserOrderRepository;
import com.scoup.server.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserOrderRepository userOrderRepository;
    private final CafeRepository cafeRepository;
    private final MenuRepository menuRepository;

    public UserDateResponseDto getUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        return UserDateResponseDto.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .build();
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void patchUser(Long userId, UpdateUserRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        user.updateUser(requestDto);
    }

    @Transactional
    public void patchUserPassword(Long userId, UpdateUserPasswordRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));
        userRepository.findFirstByPassword(requestDto.getOriginPassword())
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_PASSWORD_EXCEPTION));

        user.updateUserPassword(requestDto);
    }

    public List<MainPageCafeResponseDto> findCafe(Long id){
        User user= userRepository.findById(id)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));
        List<Long> cafeIdList=user.getCafeIdList();
        List<UserOrder> userOrderList=userOrderRepository.findByUser_Id(id);

        List<MainPageCafeResponseDto> cafeList=new ArrayList<>();

        for(int i=0; i<cafeIdList.size(); i++) {
            Cafe c = cafeRepository.findById(cafeIdList.get(i))
                    .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

            int stamp=(int)(userOrderList.stream().filter(a->a.getMenu().getCafe().equals(c)).count());
            List<String> tmpMenuList=new ArrayList<>();
            menuRepository.findByCafe_Id(cafeIdList.get(i)).stream().forEach(a->tmpMenuList.add(a.getName()));

            MainPageCafeResponseDto tmp=MainPageCafeResponseDto.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .menu(tmpMenuList)
                    .stamp(stamp)
                    .build();

            cafeList.add(tmp);
        }

        return cafeList;
    }

}
