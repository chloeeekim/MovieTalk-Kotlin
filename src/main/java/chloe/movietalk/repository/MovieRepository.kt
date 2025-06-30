package chloe.movietalk.repository

import chloe.movietalk.domain.Movie
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MovieRepository : JpaRepository<Movie, UUID> {
    fun findByTitleContaining(keyword: String, pageable: Pageable): Page<Movie>

    fun findByCodeFIMS(code: String): Movie?

    fun findByDirectorId(directorId: UUID, pageable: Pageable): Page<Movie>

    @EntityGraph(attributePaths = ["reviews"])
    override fun findById(id: UUID): Optional<Movie>

    override fun findAll(pageable: Pageable): Page<Movie>
}
