package chloe.movietalk.dto.response.review

import chloe.movietalk.domain.Review
import chloe.movietalk.dto.response.movie.MovieInfo
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class ReviewByUserResponse(
    @field:Schema(description = "리뷰 ID")
    val id: UUID,
    
    @field:Schema(description = "평점")
    val rating: Double,
    
    @field:Schema(description = "코멘트")
    val comment: String,
    
    @field:Schema(description = "영화 정보")
    val movieInfo: MovieInfo,
    
    @field:Schema(description = "좋아요 수")
    val likes: Int
) {
    companion object {
        @JvmStatic
        fun fromEntity(review: Review): ReviewByUserResponse {
            return ReviewByUserResponse(
                id = review.id!!,
                rating = review.rating,
                comment = review.comment,
                movieInfo = MovieInfo.fromEntity(review.movie!!),
                likes = review.likes
            )
        }
    }
}
