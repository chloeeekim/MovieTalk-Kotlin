package chloe.movietalk.repository

import chloe.movietalk.domain.Movie
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MovieRepository : JpaRepository<Movie, UUID> {
    fun findByTitleContaining(keyword: String, pageable: Pageable): Page<Movie>

    fun findByCodeFIMS(code: String): Movie?

    fun findByDirectorId(directorId: UUID, pageable: Pageable): Page<Movie>

    @Query("SELECT m from Movie m LEFT JOIN FETCH m.reviews WHERE m.id = :id")
    fun findByIdOrNull(@Param("id") id: UUID): Movie?

    override fun findAll(pageable: Pageable): Page<Movie>
}
