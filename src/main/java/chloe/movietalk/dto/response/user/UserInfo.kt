package chloe.movietalk.dto.response.user

import chloe.movietalk.domain.SiteUser
import chloe.movietalk.domain.SiteUser.email
import chloe.movietalk.domain.SiteUser.id
import chloe.movietalk.domain.SiteUser.nickname
import chloe.movietalk.domain.SiteUser.role
import chloe.movietalk.domain.enums.UserRole
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@Getter
@NoArgsConstructor
class UserInfo @Builder constructor(
    @field:Schema(description = "사용자 ID") private var id: UUID?, @field:Schema(
        description = "이메일"
    ) private var email: String?, @field:Schema(description = "닉네임") private var nickname: String?, @field:Schema(
        description = "권한"
    ) private var role: UserRole?
) {
    companion object {
        @JvmStatic
        fun fromEntity(user: SiteUser): UserInfo? {
            return UserInfo.builder()
                .id(user.id)
                .email(user.email)
                .nickname(user.nickname)
                .role(user.role)
                .build()
        }
    }
}