package chloe.movietalk.dto.response.review;

import chloe.movietalk.domain.Review;
import chloe.movietalk.dto.response.movie.MovieInfo;
import chloe.movietalk.dto.response.user.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ReviewDetailResponse {

    @Schema(description = "리뷰 ID")
    private UUID id;

    @Schema(description = "평점")
    private Double rating;

    @Schema(description = "코멘트")
    private String comment;

    @Schema(description = "영화 ID")
    private MovieInfo movieInfo;

    @Schema(description = "사용자 ID")
    private UserInfo userInfo;

    @Schema(description = "좋아요 수")
    private Integer likes;

    @Builder
    public ReviewDetailResponse(UUID id, Double rating, String comment, MovieInfo movieInfo, UserInfo userInfo, Integer likes) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.movieInfo = movieInfo;
        this.userInfo = userInfo;
        this.likes = likes;
    }

    public static ReviewDetailResponse fromEntity(Review review) {
        return ReviewDetailResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .movieInfo(MovieInfo.fromEntity(review.getMovie()))
                .userInfo(UserInfo.fromEntity(review.getUser()))
                .likes(review.getLikes())
                .build();
    }
}
