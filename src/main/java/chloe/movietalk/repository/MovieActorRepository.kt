package chloe.movietalk.repository

import chloe.movietalk.domain.MovieActor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieActorRepository : JpaRepository<MovieActor, Long>
