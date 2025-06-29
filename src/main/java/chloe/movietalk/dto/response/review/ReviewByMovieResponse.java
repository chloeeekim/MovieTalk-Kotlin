package chloe.movietalk.dto.response.review;

import chloe.movietalk.domain.Review;
import chloe.movietalk.dto.response.user.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ReviewByMovieResponse {

    @Schema(description = "리뷰 ID")
    private UUID id;

    @Schema(description = "평점")
    private Double rating;

    @Schema(description = "코멘트")
    private String comment;

    @Schema(description = "사용자 정보")
    private UserInfo userInfo;

    @Schema(description = "좋아요 수")
    private Integer likes;

    @Builder
    public ReviewByMovieResponse(UUID id, Double rating, String comment, UserInfo userInfo, Integer likes) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.userInfo = userInfo;
        this.likes = likes;
    }

    public static ReviewByMovieResponse fromEntity(Review review) {
        return ReviewByMovieResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userInfo(UserInfo.fromEntity(review.getUser()))
                .likes(review.getLikes())
                .build();
    }
}
