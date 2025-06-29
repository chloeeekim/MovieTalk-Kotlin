package chloe.movietalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "code_fims", length = 50, nullable = false)
    private String codeFIMS;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "prod_year")
    private Integer prodYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private Director director;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieActor> movieActors = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    private Double totalRating = 0.0;

    private Integer reviewCount = 0;

    @Builder
    public Movie(String codeFIMS,
                 String title,
                 String synopsis,
                 LocalDate releaseDate,
                 Integer prodYear,
                 Director director,
                 Double totalRating,
                 Integer reviewCount) {
        this.codeFIMS = codeFIMS;
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.prodYear = prodYear;
        this.director = director;
        this.totalRating = totalRating != null ? totalRating : 0.0;
        this.reviewCount = reviewCount != null ? reviewCount : 0;
    }

    public void updateMovie(Movie movie) {
        this.codeFIMS = movie.getCodeFIMS();
        this.title = movie.getTitle();
        this.synopsis = movie.getSynopsis();
        this.releaseDate = movie.getReleaseDate();
        this.prodYear = movie.getProdYear();
        this.director = movie.getDirector();
        this.movieActors = movie.getMovieActors();
        this.reviews = movie.getReviews();
        this.totalRating = movie.getTotalRating();
        this.reviewCount = movie.getReviewCount();
    }

    public List<Actor> getActors() {
        return movieActors.stream().map(MovieActor::getActor).toList();
    }

    public void addActor(Actor actor) {
        MovieActor movieActor = new MovieActor(this, actor);
        movieActors.add(movieActor);
        actor.getMovieActors().add(movieActor);
    }

    public void changeDirector(Director newDirector) {
        if (this.director != null) {
            this.director.getFilmography().remove(this);
        }
        this.director = newDirector;
        if (newDirector != null && !newDirector.getFilmography().contains(this)) {
            newDirector.getFilmography().add(this);
        }
    }

    public void removeDirector() {
        this.director = null;
    }

    public Double getAverageRating() {
        return reviewCount > 0 ? Math.round(totalRating / reviewCount * 10.0) / 10.0 : 0.0;
    }

    public void updateTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public void updateReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }
}