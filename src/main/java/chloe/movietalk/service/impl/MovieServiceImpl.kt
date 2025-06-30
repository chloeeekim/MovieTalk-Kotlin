package chloe.movietalk.service.impl

import chloe.movietalk.domain.Actor
import chloe.movietalk.domain.Director
import chloe.movietalk.domain.Movie
import chloe.movietalk.domain.Review
import chloe.movietalk.dto.request.MovieRequest
import chloe.movietalk.dto.response.movie.MovieDetailResponse
import chloe.movietalk.dto.response.movie.MovieInfoResponse
import chloe.movietalk.dto.response.movie.UpdateMovieResponse
import chloe.movietalk.exception.CustomException
import chloe.movietalk.exception.actor.ActorNotFoundException
import chloe.movietalk.exception.director.DirectorNotFoundException
import chloe.movietalk.exception.movie.AlreadyExistsMovieException
import chloe.movietalk.exception.movie.MovieNotFoundException
import chloe.movietalk.repository.ActorRepository
import chloe.movietalk.repository.DirectorRepository
import chloe.movietalk.repository.MovieRepository
import chloe.movietalk.service.MovieService
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Function
import java.util.function.Supplier

@Service
@Transactional
@RequiredArgsConstructor
class MovieServiceImpl : MovieService {
    private val movieRepository: MovieRepository? = null
    private val directorRepository: DirectorRepository? = null
    private val actorRepository: ActorRepository? = null

    override fun getAllMovies(pageable: Pageable): Page<MovieInfoResponse?>? {
        return movieRepository!!.findAll(pageable)
            .map<MovieInfoResponse?>(Function { obj: Movie? -> MovieInfoResponse.Companion.fromEntity() })
    }

    override fun getMovieById(id: UUID): MovieDetailResponse {
        val movie = movieRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })

        return MovieDetailResponse.fromEntity(movie, getTop3Review(movie.reviews))
    }

    override fun searchMovies(keyword: String, pageable: Pageable): Page<MovieInfoResponse?>? {
        return movieRepository!!.findByTitleContaining(keyword, pageable)
            .map<MovieInfoResponse?>(Function { obj: Movie? -> MovieInfoResponse.Companion.fromEntity() })
    }

    override fun createMovie(request: MovieRequest): MovieInfoResponse? {
        movieRepository!!.findByCodeFIMS(request.codeFIMS)
            .ifPresent({ a ->
                throw AlreadyExistsMovieException.EXCEPTION
            })

        val save = movieRepository.save<Movie>(request.toEntity(getDirectorInfo(request.directorId)))
        return MovieInfoResponse.fromEntity(save)
    }

    override fun updateMovie(id: UUID, request: MovieRequest): MovieInfoResponse? {
        val movie = movieRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })

        movie.updateMovie(request.toEntity(getDirectorInfo(request.directorId)))
        return MovieInfoResponse.fromEntity(movie)
    }

    override fun deleteMovie(id: UUID) {
        movieRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })
        movieRepository.deleteById(id)
    }

    override fun updateMovieActors(id: UUID, actorIds: MutableList<UUID?>): UpdateMovieResponse? {
        val movie = movieRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })

        movie.movieActors.clear()

        actorIds.stream()
            .map<Actor?> { l: UUID? ->
                actorRepository!!.findById(l)
                    .orElseThrow<CustomException?>(Supplier { ActorNotFoundException.EXCEPTION })
            }
            .forEach { actor: Actor? -> movie.addActor(actor!!) }

        return UpdateMovieResponse.fromEntity(movie)
    }

    override fun updateMovieDirector(id: UUID, directorId: UUID): UpdateMovieResponse? {
        val movie = movieRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })

        val director = directorRepository!!.findById(directorId)
            .orElseThrow<CustomException?>(Supplier { DirectorNotFoundException.EXCEPTION })

        movie.changeDirector(director)
        return UpdateMovieResponse.fromEntity(movie)
    }

    private fun getDirectorInfo(id: UUID?): Director? {
        if (id == null) {
            return null
        } else {
            return directorRepository!!.findById(id)
                .orElseThrow<CustomException?>(Supplier { DirectorNotFoundException.EXCEPTION })
        }
    }

    private fun getTop3Review(reviews: MutableList<Review?>): MutableList<Review?> {
        return reviews.stream()
            .sorted(Comparator.comparingInt<Review?>(Review::likes).reversed())
            .limit(3)
            .toList()
    }
}
