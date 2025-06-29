package chloe.movietalk.dto.response.review

import chloe.movietalk.domain.Review
import chloe.movietalk.domain.Review.comment
import chloe.movietalk.domain.Review.id
import chloe.movietalk.domain.Review.likes
import chloe.movietalk.domain.Review.rating
import chloe.movietalk.dto.response.movie.MovieInfo
import chloe.movietalk.dto.response.user.UserInfo
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@Getter
@NoArgsConstructor
class ReviewDetailResponse @Builder constructor(
    @field:Schema(description = "리뷰 ID") private var id: UUID?,
    @field:Schema(
        description = "평점"
    ) private var rating: Double?,
    @field:Schema(description = "코멘트") private var comment: String?,
    @field:Schema(
        description = "영화 ID"
    ) private var movieInfo: MovieInfo?,
    @field:Schema(description = "사용자 ID") private var userInfo: UserInfo?,
    @field:Schema(
        description = "좋아요 수"
    ) private var likes: Int?
) {
    companion object {
        @JvmStatic
        fun fromEntity(review: Review): ReviewDetailResponse? {
            return ReviewDetailResponse.builder()
                .id(review.id)
                .rating(review.rating)
                .comment(review.comment)
                .movieInfo(MovieInfo.fromEntity(review.movie!!))
                .userInfo(UserInfo.fromEntity(review.user))
                .likes(review.likes)
                .build()
        }
    }
}
