package chloe.movietalk.dto.response.review;

import chloe.movietalk.domain.Review;
import chloe.movietalk.dto.response.movie.MovieInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ReviewByUserResponse {

    @Schema(description = "리뷰 ID")
    private UUID id;

    @Schema(description = "평점")
    private Double rating;

    @Schema(description = "코멘트")
    private String comment;

    @Schema(description = "영화 정보")
    private MovieInfo movieInfo;

    @Schema(description = "좋아요 수")
    private Integer likes;

    @Builder
    public ReviewByUserResponse(UUID id, Double rating, String comment, MovieInfo movieInfo, Integer likes) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.movieInfo = movieInfo;
        this.likes = likes;
    }

    public static ReviewByUserResponse fromEntity(Review review) {
        return ReviewByUserResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .movieInfo(MovieInfo.fromEntity(review.getMovie()))
                .likes(review.getLikes())
                .build();
    }
}
