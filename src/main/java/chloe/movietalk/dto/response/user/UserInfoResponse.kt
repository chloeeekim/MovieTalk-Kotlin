package chloe.movietalk.dto.response.user

import chloe.movietalk.domain.SiteUser
import chloe.movietalk.domain.enums.UserRole
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class UserInfoResponse(
    @field:Schema(description = "사용자 ID")
    val id: UUID,
    
    @field:Schema(description = "이메일")
    val email: String,
    
    @field:Schema(description = "닉네임")
    val nickname: String,
    
    @field:Schema(description = "권한")
    val role: UserRole
) {
    companion object {
        @JvmStatic
        fun fromEntity(user: SiteUser): UserInfoResponse {
            return UserInfoResponse(
                id = user.id!!,
                email = user.email,
                nickname = user.nickname,
                role = user.role
            )
        }
    }
}
