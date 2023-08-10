package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundException;
import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.User;
import com.scoup.server.dto.cafe.SearchCafeResponseDto;
import com.scoup.server.repository.CafeRepository;
import com.scoup.server.repository.UserRepository;
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
}
