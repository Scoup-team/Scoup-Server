package com.scoup.server.service;


import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.controller.exception.TokenForbiddenException;
import com.scoup.server.controller.exception.UserConflictException;
import com.scoup.server.domain.User;
import com.scoup.server.dto.auth.SignupRequestDTO;
import com.scoup.server.dto.auth.SignupResponseDTO;
import com.scoup.server.dto.auth.TokenServiceVO;
import com.scoup.server.repository.UserRepository;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

    @Transactional
    public TokenServiceVO reIssueToken(@NotNull TokenServiceVO token){
        boolean isAccessTokenExpired = jwtService.isExpired(token.getAccessToken());
        boolean isRefreshTokenExpired = jwtService.isExpired(token.getRefreshToken());

        if(isAccessTokenExpired) {
            if(isRefreshTokenExpired){
                throw new TokenForbiddenException(ErrorMessage.EXPIRED_ALL_TOKEN_EXCEPTION);
            }
            final String refreshToken = token.getRefreshToken();
            return setNewAccessToken(refreshToken);
        }

        throw new TokenForbiddenException(ErrorMessage.VALID_ALL_TOKEN_EXCEPTION);
    }

    public TokenServiceVO registerToken(User user) {
        TokenServiceVO serviceToken = jwtService.createServiceToken(user.getId());

        return serviceToken;
    }

    public TokenServiceVO setNewAccessToken(String refreshToken) {
        final Long userId = jwtService.getUserId(refreshToken);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        final String newAccessToken = jwtService.createAccessToken(user.getId());
        final TokenServiceVO token = TokenServiceVO.of(newAccessToken, refreshToken);

        return token;
    }

}
