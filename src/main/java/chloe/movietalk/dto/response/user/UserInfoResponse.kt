package chloe.movietalk.dto.response.user;

import chloe.movietalk.domain.SiteUser;
import chloe.movietalk.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UserInfoResponse {

    @Schema(description = "사용자 ID")
    private UUID id;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "권한")
    private UserRole role;

    @Builder
    public UserInfoResponse(UUID id, String email, String nickname, UserRole role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    public static UserInfoResponse fromEntity(SiteUser user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
    }
}
