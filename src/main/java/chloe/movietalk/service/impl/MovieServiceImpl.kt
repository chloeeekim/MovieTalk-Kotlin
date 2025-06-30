package chloe.movietalk.service.impl

import chloe.movietalk.domain.Director
import chloe.movietalk.domain.Review
import chloe.movietalk.dto.request.MovieRequest
import chloe.movietalk.dto.response.movie.MovieDetailResponse
import chloe.movietalk.dto.response.movie.MovieInfoResponse
import chloe.movietalk.dto.response.movie.UpdateMovieResponse
import chloe.movietalk.exception.actor.ActorNotFoundException
import chloe.movietalk.exception.director.DirectorNotFoundException
import chloe.movietalk.exception.movie.AlreadyExistsMovieException
import chloe.movietalk.exception.movie.MovieNotFoundException
import chloe.movietalk.repository.ActorRepository
import chloe.movietalk.repository.DirectorRepository
import chloe.movietalk.repository.MovieRepository
import chloe.movietalk.service.MovieService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class MovieServiceImpl(
    private val movieRepository: MovieRepository,
    private val directorRepository: DirectorRepository,
    private val actorRepository: ActorRepository
) : MovieService {
    override fun getAllMovies(pageable: Pageable): Page<MovieInfoResponse> {
        return movieRepository.findAll(pageable).map { MovieInfoResponse.fromEntity(it) }
    }

    override fun getMovieById(id: UUID): MovieDetailResponse {
        val movie = movieRepository.findByIdOrNull(id)
            ?: throw MovieNotFoundException

        return MovieDetailResponse.fromEntity(movie, getTop3Review(movie.reviews))
    }

    override fun searchMovies(keyword: String, pageable: Pageable): Page<MovieInfoResponse> {
        return movieRepository.findByTitleContaining(keyword, pageable)
            .map { MovieInfoResponse.fromEntity(it) }
    }

    override fun createMovie(request: MovieRequest): MovieInfoResponse {
        movieRepository.findByCodeFIMS(request.codeFIMS)?.let { throw AlreadyExistsMovieException }

        val save = movieRepository.save(request.toEntity(getDirectorInfo(request.directorId)))
        return MovieInfoResponse.fromEntity(save)
    }

    override fun updateMovie(id: UUID, request: MovieRequest): MovieInfoResponse {
        val movie = movieRepository.findByIdOrNull(id)
            ?: throw MovieNotFoundException

        movie.updateMovie(request.toEntity(getDirectorInfo(request.directorId)))
        return MovieInfoResponse.fromEntity(movie)
    }

    override fun deleteMovie(id: UUID) {
        movieRepository.findByIdOrNull(id)
            ?: throw MovieNotFoundException
        movieRepository.deleteById(id)
    }

    override fun updateMovieActors(id: UUID, actorIds: List<UUID>): UpdateMovieResponse {
        val movie = movieRepository.findByIdOrNull(id)
            ?: throw MovieNotFoundException

        movie.movieActors.clear()

        actorIds
            .map { actorRepository.findByIdOrNull(it) ?: throw ActorNotFoundException }
            .forEach { movie.addActor(it) }

        return UpdateMovieResponse.fromEntity(movie)
    }

    override fun updateMovieDirector(id: UUID, directorId: UUID): UpdateMovieResponse {
        val movie = movieRepository.findByIdOrNull(id)
            ?: throw MovieNotFoundException

        val director = directorRepository.findByIdOrNull(directorId)
            ?: throw DirectorNotFoundException

        movie.changeDirector(director)
        return UpdateMovieResponse.fromEntity(movie)
    }

    private fun getDirectorInfo(id: UUID?): Director? {
        return id?.let { directorRepository.findByIdOrNull(id) ?: throw DirectorNotFoundException }
    }

    private fun getTop3Review(reviews: MutableList<Review>): List<Review> {
        return reviews.sortedByDescending { it.likes }.take(3)
    }
}
