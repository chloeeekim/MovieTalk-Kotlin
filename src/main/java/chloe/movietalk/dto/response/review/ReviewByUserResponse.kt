package chloe.movietalk.dto.response.review

import chloe.movietalk.domain.Review
import chloe.movietalk.domain.Review.comment
import chloe.movietalk.domain.Review.id
import chloe.movietalk.domain.Review.likes
import chloe.movietalk.domain.Review.rating
import chloe.movietalk.dto.response.movie.MovieInfo
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@Getter
@NoArgsConstructor
class ReviewByUserResponse @Builder constructor(
    @field:Schema(description = "리뷰 ID") private var id: UUID?, @field:Schema(
        description = "평점"
    ) private var rating: Double?, @field:Schema(description = "코멘트") private var comment: String?, @field:Schema(
        description = "영화 정보"
    ) private var movieInfo: MovieInfo?, @field:Schema(description = "좋아요 수") private var likes: Int?
) {
    companion object {
        @JvmStatic
        fun fromEntity(review: Review): ReviewByUserResponse? {
            return ReviewByUserResponse.builder()
                .id(review.id)
                .rating(review.rating)
                .comment(review.comment)
                .movieInfo(MovieInfo.fromEntity(review.movie!!))
                .likes(review.likes)
                .build()
        }
    }
}
