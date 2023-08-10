package com.scoup.server.service;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundException;
import com.scoup.server.domain.User;
import com.scoup.server.dto.user.UpdateUserRequestDto;
import com.scoup.server.dto.user.UserDateResponseDto;
import com.scoup.server.repository.UserRepository;
import java.util.Optional;
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

}
