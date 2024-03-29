package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.Menu;
import com.scoup.server.domain.User;
import com.scoup.server.domain.UserOrder;
import com.scoup.server.dto.cafe.AdminCafeReponseDto;
import com.scoup.server.dto.mainPage.MainPageCafeResponseDto;
import com.scoup.server.dto.stamp.StampResponseDto;
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

import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scoup.server.config.hashconvert.PasswordConverter.encode;

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
        /*
        userRepository.findFirstByPassword(requestDto.getOriginPassword())
            .orElseThrow(
                () -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_PASSWORD_EXCEPTION));
         */

        String newPassword=encode(requestDto.getNewPassword());

        if(newPassword.equals(user.getPassword())){
            throw new NotFoundDataException(ErrorMessage.SAME_PASSWORD_EXCEPTION);
        }

        user.updateUserPassword(newPassword);
    }

    public List<MainPageCafeResponseDto> getCafe(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        List<Long> cafeIdList = user.getCafeIdList();
        List<UserOrder> userOrderList = userOrderRepository.findByUser_Id(id);

        List<MainPageCafeResponseDto> cafeList = new ArrayList<>();

        //스탬프 처리 어떻게 할 것인가 유저 오더를 유저 id로 검색해서 스탬프 가져온 다음
        //카페별로 갈라서 출력하면 될 것 같은데 이걸 어떻게 이쁘게 출력할지...
        for (int i = 0; i < cafeIdList.size(); i++) {
            Cafe c = cafeRepository.findById(cafeIdList.get(i))
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION));

            //유저 아이디로 검색한 오더리스트 카페로 검색해 추려냄
            List<UserOrder> tmpOrderList=
                    userOrderList.stream().filter(a ->a.getMenu().getCafe().equals(c)).collect(
                        Collectors.toList());

            List<Long> tmpStampIdList=new ArrayList<>();
            tmpOrderList.stream().distinct().forEach(a->tmpStampIdList.add(a.getStamp().getId()));//정상 출력되는거 확인

            List<StampResponseDto> tmpStampList=new ArrayList<>();
            //스탬프 id로 유저 오더 검색해서 메뉴 찾으면 될듯?

            //이거 시간 되면 더 깔끔하게...
            for(int j=0; j<tmpStampIdList.size(); j++){
                //뭐라 하지
                List<String> tmpMenuList=userOrderRepository.findByStamp_Id(tmpStampIdList.get(j)).stream()
                        .map(a->a.getMenu().getName()).collect(Collectors.toList());

                StampResponseDto tmpDto=StampResponseDto.builder()
                        .stampId(tmpStampIdList.get(j))
                        .menu(tmpMenuList)
                        .build();

                tmpStampList.add(tmpDto);
            }

            List<Menu> tmpMenuList = menuRepository.findByCafe_Id(cafeIdList.get(i));

            //추천 메뉴 3개 뽑는 코드
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

            MainPageCafeResponseDto tmp = MainPageCafeResponseDto.builder()
                .shopId(c.getId())
                .name(c.getName())
                .cafeImageUrl(c.getImageUrl())
                .menu(menuNameList)
                .menuImageUrl(menuImgList)
                .stamp(tmpStampList)
                .build();

            cafeList.add(tmp);
        }

        return cafeList;
    }

    public AdminCafeReponseDto getAdminCafe(Long adminUserId){

        List<Cafe> tmpCafe=cafeRepository.findByUser_Id(adminUserId);

        if(tmpCafe.size()!=1){
            throw new NotFoundDataException(ErrorMessage.NOT_FOUND_CAFE_EXCEPTION);
        }

        AdminCafeReponseDto tmp=AdminCafeReponseDto.builder()
                .shopId(tmpCafe.get(0).getId())
                .shopName(tmpCafe.get(0).getName())
                //.shopBranch(c)
                .shopAddress(tmpCafe.get(0).getLocation())
                .shopImageUrl(tmpCafe.get(0).getImageUrl())
                .runningTime(tmpCafe.get(0).getRunningTime())
                .phoneNumber(tmpCafe.get(0).getPhoneNumber())
                .licenseeNumber(tmpCafe.get(0).getLicenseeNumber())
                .build();

        return tmp;
    }
}
