package com.scoup.server.controller;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.SuccessMessage;
import com.scoup.server.config.resolver.ServiceToken;
import com.scoup.server.dto.auth.SigninRequestDTO;
import com.scoup.server.dto.auth.SignupRequestDTO;
import com.scoup.server.dto.auth.SignupResponseDTO;
import com.scoup.server.dto.auth.TokenServiceVO;
import com.scoup.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponseDTO> signup(
        @RequestBody SignupRequestDTO requestDTO
    ) {
        SignupResponseDTO data = authService.signupService(requestDTO);
        return ApiResponse.success(SuccessMessage.SIGNUP_SUCCESS, data);
    }

    @PostMapping("/auth/token")
    public ApiResponse<TokenServiceVO> reIssueToken(@ServiceToken TokenServiceVO token) {
        TokenServiceVO data = authService.reIssueToken(token);
        return ApiResponse.success(SuccessMessage.TOKEN_RE_ISSUE_SUCCESS, data);
    }

    @PostMapping("/auth/signin")
    public ApiResponse<SignupResponseDTO> signin(
        @RequestBody SigninRequestDTO requestDTO
    ) {
        SignupResponseDTO data = authService.signinService(requestDTO, false);
        return ApiResponse.success(SuccessMessage.LOGIN_SUCCESS, data);
    }

    @PostMapping("/admin/auth/signin")
    public ApiResponse<SignupResponseDTO> adminSignin(
        @RequestBody SigninRequestDTO requestDTO
    ) {
        SignupResponseDTO data = authService.signinService(requestDTO, true);
        return ApiResponse.success(SuccessMessage.LOGIN_SUCCESS, data);
    }
}
