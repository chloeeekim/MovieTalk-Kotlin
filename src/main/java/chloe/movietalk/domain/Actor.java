package chloe.movietalk.domain;

import chloe.movietalk.domain.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Actor extends Person {

    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieActor> movieActors = new ArrayList<>();

    @Builder
    public Actor(String name, Gender gender, String country) {
        super(name, gender, country);
    }

    public void updateActor(Actor actor) {
        super.updatePerson(actor.getName(), actor.getGender(), actor.getCountry());
        this.movieActors = actor.getMovieActors();
    }

    public List<Movie> getMovies() {
        return movieActors.stream().map((MovieActor::getMovie)).toList();
    }

    public void addMovie(Movie movie) {
        MovieActor movieActor = new MovieActor(movie, this);
        movieActors.add(movieActor);
        movie.getMovieActors().add(movieActor);
    }
}
