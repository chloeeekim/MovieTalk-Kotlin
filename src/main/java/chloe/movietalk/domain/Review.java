package chloe.movietalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private SiteUser user;

    private Integer likes = 0;

    @Builder
    public Review(Double rating, String comment, Movie movie, SiteUser user, Integer likes) {
        this.rating = rating;
        this.comment = comment;
        this.movie = movie;
        this.user = user;
        this.likes = likes != null ? likes : 0;
    }

    public void updateReview(Review review) {
        this.rating = review.getRating();
        this.comment = review.getComment();
    }

    public void updateTotalLikes(Integer likes) {
        this.likes = likes;
    }
}
