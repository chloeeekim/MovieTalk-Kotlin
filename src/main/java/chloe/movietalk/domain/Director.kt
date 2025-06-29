package chloe.movietalk.domain

import chloe.movietalk.domain.enums.Gender
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
class Director(
    name: String,
    gender: Gender,
    country: String
) : Person(name, gender, country) {
    @OneToMany(mappedBy = "director")
    var filmography: MutableList<Movie> = mutableListOf()

    fun updateDirector(director: Director) {
        super.updatePerson(director.name, director.gender, director.country)
        this.filmography = director.filmography
    }
}
