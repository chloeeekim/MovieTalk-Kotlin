package chloe.movietalk.dto.request

import chloe.movietalk.common.HalfPointStep
import chloe.movietalk.domain.Movie
import chloe.movietalk.domain.Review
import chloe.movietalk.domain.SiteUser
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@Getter
@NoArgsConstructor
class CreateReviewRequest @Builder constructor(
    @field:Schema(
        description = "평점 (0.5점 단위)",
        example = "3.5"
    ) @field:HalfPointStep private var rating: @Digits(
        integer = 1,
        fraction = 1,
        message = "평점은 소수점 아래 한자리수여야 합니다."
    ) @DecimalMax(value = "5.0", message = "평점은 5.0점 이하여야 합니다.") @DecimalMin(
        value = "0.5",
        message = "평점은 0.5점 이상이어야 합니다."
    ) @NotNull(message = "평점이 입력되지 않았습니다.") Double?, @field:Schema(
        description = "코멘트",
        example = "좋은 영화입니다."
    ) private var comment: String?, @field:Schema(
        description = "영화 ID",
        example = "1"
    ) private var movieId: @NotNull(message = "영화가 입력되지 않았습니다.") UUID?, @field:Schema(
        description = "사용자 ID",
        example = "1"
    ) private var userId: @NotNull(message = "사용자가 입력되지 않았습니다.") UUID?
) {
    fun toEntity(movie: Movie?, user: SiteUser?): Review {
        return Review.builder()
            .rating(this.rating)
            .comment(this.comment)
            .movie(movie)
            .user(user)
            .build()
    }
}
