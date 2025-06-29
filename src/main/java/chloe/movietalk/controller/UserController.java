package chloe.movietalk.controller;

import chloe.movietalk.dto.request.LoginRequest;
import chloe.movietalk.dto.request.SignupRequest;
import chloe.movietalk.dto.response.user.UserInfoResponse;
import chloe.movietalk.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {

    @PostMapping("/signup")
    @Operation(summary = "Sign up", description = "회원가입을 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = UserInfoResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자입니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<UserInfoResponse> signup(
            @Schema(implementation = SignupRequest.class)
            @RequestBody @Valid SignupRequest request
    );

    @PostMapping("/login")
    @Operation(summary = "Log in", description = "로그인을 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = UserInfoResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "해당하는 사용자가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "사용자를 인증할 수 없습니다. (e.g., 잘못된 비밀번호입니다.)",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<UserInfoResponse> login(
            @Schema(implementation = LoginRequest.class)
            @RequestBody @Valid LoginRequest loginRequest,

            HttpServletRequest request,
            HttpServletResponse response
    );

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Access Token을 새로 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {}),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 Refresh Token입니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당하는 사용자가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Void> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    );

    @PostMapping("/logout")
    @Operation(summary = "Log out", description = "로그아웃을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {}),
            @ApiResponse(responseCode = "401", description = "계정에 로그인 해야 합니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Void> logout(
            HttpServletRequest request
    );
}
