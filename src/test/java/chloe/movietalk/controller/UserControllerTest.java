package chloe.movietalk.controller;

import chloe.movietalk.domain.SiteUser;
import chloe.movietalk.dto.request.LoginRequest;
import chloe.movietalk.dto.request.SignupRequest;
import chloe.movietalk.exception.auth.AuthErrorCode;
import chloe.movietalk.exception.global.GlobalErrorCode;
import chloe.movietalk.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Test
    @DisplayName("회원가입 성공")
    public void signup() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@movietalk.com")
                .password("password")
                .nickname("test")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.email").value(request.getEmail()))
                .andExpect(jsonPath("data.nickname").value(request.getNickname()));
    }

    @Test
    @DisplayName("회원가입 실패 : 잘못된 이메일 형식")
    public void signupFailure1() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test")
                .password("password")
                .nickname("test")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("회원가입 실패 : 이메일 미입력")
    public void signupFailure2() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .password("password")
                .nickname("test")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("회원가입 실패 : 비밀번호 미입력")
    public void signupFailure3() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@movietalk.com")
                .nickname("test")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("회원가입 실패 : 닉네임 미입력")
    public void signupFailure4() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@movietalk.com")
                .password("password")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("회원가입 실패 : 이미 존재하는 사용자")
    public void signupFailure5() throws Exception {
        // given
        SiteUser user = getUserForTest();

        SignupRequest request = SignupRequest.builder()
                .email("test@movietalk.com")
                .password("password")
                .nickname("test")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        AuthErrorCode errorCode = AuthErrorCode.ALREADY_EXISTS_USER;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("로그인 성공")
    public void login() throws Exception {
        // given
        SiteUser user = getUserForTest();

        LoginRequest request = LoginRequest.builder()
                .email("test@movietalk.com")
                .password("password")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value(user.getId().toString()))
                .andExpect(jsonPath("data.email").value(user.getEmail()))
                .andExpect(jsonPath("data.nickname").value(user.getNickname()));
    }

    @Test
    @DisplayName("로그인 실패 : 존재하지 않는 사용자")
    public void loginFailure1() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@movietalk.com")
                .password("password")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        AuthErrorCode errorCode = AuthErrorCode.USER_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("로그인 실패 : 일치하지 않는 비밀번호")
    public void loginFailure2() throws Exception {
        // given
        SiteUser user = getUserForTest();
        LoginRequest request = LoginRequest.builder()
                .email("test@movietalk.com")
                .password("abc")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        AuthErrorCode errorCode = AuthErrorCode.INVALID_PASSWORD;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    private SiteUser getUserForTest() {
        return userRepository.save(SiteUser.builder()
                .email("test@movietalk.com")
                .passwordHash(encoder.encode("password"))
                .nickname("test")
                .build());
    }
}
