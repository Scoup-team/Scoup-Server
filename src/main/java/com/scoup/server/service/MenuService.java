package com.scoup.server.service;

import java.util.ArrayList;
import java.util.List;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.domain.Stamp;
import com.scoup.server.domain.UserOrder;
import com.scoup.server.dto.menu.MenuDto;
import com.scoup.server.dto.menu.MenuResponseDto;
import com.scoup.server.repository.StampRepository;
import com.scoup.server.repository.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

	private final UserOrderRepository userOrderRepository;
	private final StampRepository stampRepository;

	public MenuResponseDto getMenuList(Long stampId){
		Stamp stamp=stampRepository.findById(stampId)
				.orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_COUPON_EXCEPTION));

		List<UserOrder> userOrderList=userOrderRepository.findByStamp_Id(stampId);//중복 되는거 확인
		//이제 메뉴 리턴하는데 수량이랑 가격 데이터를 같이 줘야함

		List<MenuDto> tmpMenuList=new ArrayList<>();

		for(int i=0; i<userOrderList.size(); i++){
			MenuDto tmp=MenuDto.builder()
					.id(userOrderList.get(i).getMenu().getId())
					.name(userOrderList.get(i).getMenu().getName())
					.price(userOrderList.get(i).getMenu().getPrice())
					.num(1)
					.build();

			boolean isExist=tmpMenuList.stream().anyMatch(a->a.getId().equals(tmp.getId()));

			if(!isExist){
				tmpMenuList.add(tmp);
			}else{
				tmpMenuList.stream().filter(a->a.getId().equals(tmp.getId())).findFirst()
						.orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_MENU_EXCEPTION)).addNum();

			}

		}

		return MenuResponseDto.builder()
				.id(stamp.getId())
				.cafeId(stamp.getCafe().getId())
				.cardName(stamp.getCardName())
				.cardNum(stamp.getCardNum())
				.cafeName(stamp.getCafe().getName())
				.cafeLocation(stamp.getCafe().getLocation())
				.cafeImageUrl(stamp.getCafe().getImageUrl())
				.cafeLicenseeNumber(stamp.getCafe().getLicenseeNumber())
				.cafePhoneNumber(stamp.getCafe().getPhoneNumber())
				.masterUser(stamp.getCafe().getUser().getName())
				.menu(tmpMenuList)
				.build();
	}

}