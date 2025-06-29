package chloe.movietalk.dto.response.review

import chloe.movietalk.domain.Review
import chloe.movietalk.dto.response.movie.MovieInfo
import chloe.movietalk.dto.response.user.UserInfo
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class ReviewDetailResponse(
    @field:Schema(description = "리뷰 ID")
    val id: UUID,
    
    @field:Schema(description = "평점")
    val rating: Double,
    
    @field:Schema(description = "코멘트")
    val comment: String,
    
    @field:Schema(description = "영화 ID")
    val movieInfo: MovieInfo,
    
    @field:Schema(description = "사용자 ID")
    val userInfo: UserInfo,
    
    @field:Schema(description = "좋아요 수")
    val likes: Int
) {
    companion object {
        @JvmStatic
        fun fromEntity(review: Review): ReviewDetailResponse {
            return ReviewDetailResponse(
                id = review.id!!,
                rating = review.rating,
                comment = review.comment,
                movieInfo = MovieInfo.fromEntity(review.movie!!),
                userInfo = UserInfo.fromEntity(review.user!!),
                likes = review.likes
            )
        }
    }
}
