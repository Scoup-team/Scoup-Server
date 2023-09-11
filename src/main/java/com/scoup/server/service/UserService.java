package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.Menu;
import com.scoup.server.domain.User;
import com.scoup.server.domain.UserOrder;
import com.scoup.server.dto.cafe.AdminCafeReponseDto;
import com.scoup.server.dto.mainPage.MainPageCafeResponseDto;
import com.scoup.server.dto.user.UpdateUserPasswordRequestDto;
import com.scoup.server.dto.user.UpdateUserRequestDto;
import com.scoup.server.dto.user.UserDateResponseDto;
import com.scoup.server.repository.CafeRepository;
import com.scoup.server.repository.MenuRepository;
import com.scoup.server.repository.UserOrderRepository;
import com.scoup.server.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
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
            .orElseThrow(
                () -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_PASSWORD_EXCEPTION));

        user.updateUserPassword(requestDto);
    }

    public List<MainPageCafeResponseDto> findCafe(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));
        List<Long> cafeIdList = user.getCafeIdList();
        List<UserOrder> userOrderList = userOrderRepository.findByUser_Id(id);

        List<MainPageCafeResponseDto> cafeList = new ArrayList<>();

        for (int i = 0; i < cafeIdList.size(); i++) {
            Cafe c = cafeRepository.findById(cafeIdList.get(i))
                .orElseThrow(
                    () -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

            int stamp =
                (int) (userOrderList.stream().filter(a -> a.getMenu().getCafe().equals(c)).count());
            List<Menu> tmpMenuList = menuRepository.findByCafe_Id(cafeIdList.get(i));
            tmpMenuList.sort(new Comparator<Menu>() {
                @Override
                public int compare(Menu o1, Menu o2) {
                    return
                        Integer.compare(o1.getUserOrderList().size(), o2.getUserOrderList().size())
                            ==1 ? -1 :
                            Integer.compare(o1.getUserOrderList().size(),
                                o2.getUserOrderList().size())==0 ? 0 : 1;
                }
            });

            List<String> menuNameList = new ArrayList<>();
            List<String> menuImgList = new ArrayList<>();
            if (tmpMenuList.size() >= 3) {
                tmpMenuList.stream().limit(3).forEach(a -> menuNameList.add(a.getName()));
                tmpMenuList.stream().limit(3).forEach(a -> menuImgList.add(a.getImageUrl()));
            } else {
                tmpMenuList.stream().forEach(a -> menuNameList.add(a.getName()));
                tmpMenuList.stream().forEach(a -> menuImgList.add(a.getImageUrl()));
            }

            //menuRepository.findByCafe_Id(cafeIdList.get(i)).stream().forEach(a->tmpMenuList.add(a.getName()));

            MainPageCafeResponseDto tmp = MainPageCafeResponseDto.builder()
                .shopId(c.getId())
                .name(c.getName())
                .menu(menuNameList)
                .imageUrl(menuImgList)
                .stamp(stamp)
                .build();

            cafeList.add(tmp);
        }

        return cafeList;
    }

    public List<AdminCafeReponseDto> getAdminCafe(Long adminUserId){
        User adminUser=userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        if(!adminUser.getMaster()){
            throw new NotFoundDataException(ErrorMessage.NOT_ADMIN_EXCEPTION);
        }

        List<Long> cafeIdList=adminUser.getCafeIdList();
        List<AdminCafeReponseDto> cafeList=new ArrayList<>();

        for(int i=0; i<cafeIdList.size(); i++){
            Cafe c=cafeRepository.findById(cafeIdList.get(i))
                    .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

            AdminCafeReponseDto tmp=AdminCafeReponseDto.builder()
                    .shopId(c.getId())
                    .shopName(c.getName())
                    //.shopBranch(c)
                    .shopAddress(c.getLocation())
                    .shopImageUrl(c.getImageUrl())
                    .build();
            cafeList.add(tmp);
        }

        return cafeList;
    }
}
