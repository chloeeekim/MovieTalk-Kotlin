package chloe.movietalk.service

import chloe.movietalk.dto.request.MovieRequest
import chloe.movietalk.dto.response.movie.MovieDetailResponse
import chloe.movietalk.dto.response.movie.MovieInfoResponse
import chloe.movietalk.dto.response.movie.UpdateMovieResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface MovieService {
    fun getAllMovies(pageable: Pageable): Page<MovieInfoResponse>

    fun getMovieById(id: UUID): MovieDetailResponse

    fun searchMovies(keyword: String, pageable: Pageable): Page<MovieInfoResponse>

    fun createMovie(request: MovieRequest): MovieInfoResponse

    fun updateMovie(id: UUID, request: MovieRequest): MovieInfoResponse

    fun deleteMovie(id: UUID)

    fun updateMovieActors(id: UUID, actorIds: List<UUID>): UpdateMovieResponse

    fun updateMovieDirector(id: UUID, directorId: UUID): UpdateMovieResponse
}
