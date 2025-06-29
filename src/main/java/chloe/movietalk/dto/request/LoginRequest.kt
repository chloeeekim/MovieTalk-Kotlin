package chloe.movietalk.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

class LoginRequest(
    @Schema(description = "이메일", example = "abc@movietalk.com")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    val email: String,

    @Schema(description = "비밀번호", example = "password")
    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    val password:  String
)
