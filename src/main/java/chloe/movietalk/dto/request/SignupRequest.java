package chloe.movietalk.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Schema(description = "이메일", example = "abc@movietalk.com")
    private String email;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @Schema(description = "비밀번호", example = "password")
    private String password;

    @NotBlank(message = "닉네임이 입력되지 않았습니다.")
    @Schema(description = "닉네임", example = "nickname")
    private String nickname;

    @Builder
    public SignupRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
