package com.scoup.server.service;


import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.controller.exception.TokenForbiddenException;
import com.scoup.server.controller.exception.UserConflictException;
import com.scoup.server.controller.exception.UserForbiddenException;
import com.scoup.server.domain.User;
import com.scoup.server.dto.auth.SigninRequestDTO;
import com.scoup.server.dto.auth.SignupRequestDTO;
import com.scoup.server.dto.auth.SignupResponseDTO;
import com.scoup.server.dto.auth.TokenServiceVO;
import com.scoup.server.repository.UserRepository;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.scoup.server.config.hashconvert.PasswordConverter.encode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public SignupResponseDTO signupService(SignupRequestDTO requestDTO) {
        validateUserData(requestDTO);

        final User newUser = userRepository.save(User.of(requestDTO, false));
        TokenServiceVO tokenServiceVO = registerToken(newUser);

        return SignupResponseDTO.builder()
            .nickname(newUser.getNickname())
            .accessToken(tokenServiceVO.getAccessToken())
            .refreshToken(tokenServiceVO.getRefreshToken())
            .build();
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

    @Transactional
    public SignupResponseDTO adminSignupService(SignupRequestDTO requestDTO) {
        validateUserData(requestDTO);

        final User newUser = userRepository.save(User.of(requestDTO, true));
        TokenServiceVO tokenServiceVO = registerToken(newUser);

        return SignupResponseDTO.builder()
            .nickname(newUser.getNickname())
            .accessToken(tokenServiceVO.getAccessToken())
            .refreshToken(tokenServiceVO.getRefreshToken())
            .build();
    }

    @Transactional
    public SignupResponseDTO signinService(SigninRequestDTO requestDTO, Boolean isMaster) {
        /*
        이게 코드가 데이터베이스 상에서 검색되는 비밀번호가 있는지 확인한 뒤, 없으면 로그인 실패하는 로직이 맞나요?
        데이터베이스 암호화하는 김에 이메일로 사용자를 찾고, 그 사용자의 비밀번호와 입력한 비밀번호가 일치하는지 확인하는 로직으로 바꿔놨습니다.
        혹시 영 아니면 피드백 부탁드립니다...

        userRepository.findFirstByPassword(requestDTO.getPassword())
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));
         */

        User user = userRepository.findByEmail(requestDTO.getEmail())
            .orElseThrow(() -> new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_EXCEPTION));

        String cmpPassword=encode(requestDTO.getPassword());

        //sha-256 적용한 입력값과 비번이 일치하지 않으면 예외 처리
        if(!user.getPassword().equals(cmpPassword)){
            throw new NotFoundDataException(ErrorMessage.NOT_FOUND_USER_PASSWORD_EXCEPTION);
        }

        if(isMaster && !user.getMaster()) {
            throw new UserForbiddenException(ErrorMessage.FORBIDDEN_ADMIN_EXCEPTION);
        }
        if(!isMaster && user.getMaster()) {
            throw new UserForbiddenException(ErrorMessage.FORBIDDEN_USER_EXCEPTION);
        }
        TokenServiceVO tokenServiceVO = registerToken(user);

        return SignupResponseDTO.builder()
            .nickname(user.getNickname())
            .accessToken(tokenServiceVO.getAccessToken())
            .refreshToken(tokenServiceVO.getRefreshToken())
            .build();
    }

    public void validateUserData(SignupRequestDTO requestDTO) {
        /*
        userRepository.findFirstByPassword(requestDTO.getPassword())
            .ifPresent(action -> {
                throw new UserConflictException(ErrorMessage.CONFLICT_USER_PASSWORD_EXCEPTION);
            });
         */


        userRepository.findByNickname(requestDTO.getNickname())
            .ifPresent(action -> {
                throw new UserConflictException(ErrorMessage.CONFLICT_USER_NICKNAME_EXCEPTION);
            });

    }

}
