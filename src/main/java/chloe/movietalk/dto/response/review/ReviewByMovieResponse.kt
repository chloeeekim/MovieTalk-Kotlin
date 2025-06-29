package chloe.movietalk.dto.response.review

import chloe.movietalk.domain.Review
import chloe.movietalk.dto.response.user.UserInfo
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class ReviewByMovieResponse(
    @field:Schema(description = "리뷰 ID")
    val id: UUID,
    
    @field:Schema(description = "평점")
    val rating: Double,
    
    @field:Schema(description = "코멘트")
    val comment: String,
    
    @field:Schema(description = "사용자 정보")
    val userInfo: UserInfo,
    
    @field:Schema(description = "좋아요 수")
    val likes: Int
) {
    companion object {
        @JvmStatic
        fun fromEntity(review: Review): ReviewByMovieResponse {
            return ReviewByMovieResponse(
                id = review.id!!,
                rating = review.rating,
                comment = review.comment,
                userInfo = UserInfo.fromEntity(review.user),
                likes = review.likes
            )
        }
    }
}
