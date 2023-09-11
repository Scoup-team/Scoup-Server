package com.scoup.server.service;


import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.UserConflictException;
import com.scoup.server.domain.User;
import com.scoup.server.dto.auth.SignupRequestDTO;
import com.scoup.server.dto.auth.SignupResponseDTO;
import com.scoup.server.dto.auth.TokenServiceVO;
import com.scoup.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public SignupResponseDTO signupService(SignupRequestDTO requestDTO) {
        validateUserData(requestDTO);

        final User newUser = userRepository.save(User.of(requestDTO));
        TokenServiceVO tokenServiceVO = registerToken(newUser);

        return SignupResponseDTO.builder()
            .accessToken(tokenServiceVO.getAccessToken())
            .refreshToken(tokenServiceVO.getRefreshToken())
            .build();
    }

    public void validateUserData(SignupRequestDTO requestDTO) {
        userRepository.findFirstByPassword(requestDTO.getPassword())
            .ifPresent(action -> {
                throw new UserConflictException(ErrorMessage.CONFLICT_USER_PASSWORD_EXCEPTION);
            });

        userRepository.findByNickname(requestDTO.getNickname())
            .ifPresent(action -> {
                throw new UserConflictException(ErrorMessage.CONFLICT_USER_NICKNAME_EXCEPTION);
            });

    }

    public TokenServiceVO registerToken(User user) {
        TokenServiceVO serviceToken = jwtService.createServiceToken(user.getId());

        return serviceToken;
    }

}
