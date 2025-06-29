package chloe.movietalk.domain;

import chloe.movietalk.domain.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Director extends Person {

    @OneToMany(mappedBy = "director")
    private List<Movie> filmography = new ArrayList<>();

    @Builder
    public Director(String name, Gender gender, String country) {
        super(name, gender, country);
    }

    public void updateDirector(Director director) {
        super.updatePerson(director.getName(), director.getGender(), director.getCountry());
        this.filmography = director.filmography;
    }
}
