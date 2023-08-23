package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundException;
import com.scoup.server.domain.Menu;
import com.scoup.server.domain.UserOrder;
import com.scoup.server.dto.menu.MenuResponseDto;
import com.scoup.server.repository.MenuRepository;
import com.scoup.server.repository.UserOrderRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
	private final UserOrderRepository userOrderRepository;

	public MenuResponseDto getMenuList(Long shopId, Long orderId){
		List<Menu> menuList=menuRepository.findByCafe_Id(shopId);

		UserOrder userOrder=userOrderRepository.findById(orderId)
				.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

		List<Menu> orderMenus=new ArrayList<>();

		for(int i=0; i<menuList.size(); i++){
			if(menuList.get(i).getUserOrderList().contains(userOrder)){
				Menu tmp=Menu.builder()
					.id(menuList.get(0).getId())
					.price(menuList.get(0).getPrice())
					.userOrderList(menuList.get(0).getUserOrderList())
					.name(menuList.get(0).getName())
					.cafe(menuList.get(0).getCafe())
					.build();
				orderMenus.add(tmp);
			}
		}

		if(orderMenus.isEmpty()){
			throw new NotFoundException(ErrorMessage.NOT_FOUND_MENU_EXCEPTION);
		}

		return MenuResponseDto.builder()
				.menuList(orderMenus)
				.build();
	}

}