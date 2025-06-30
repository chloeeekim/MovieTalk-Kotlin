package chloe.movietalk.controller.impl

import chloe.movietalk.controller.MovieController
import chloe.movietalk.dto.request.MovieRequest
import chloe.movietalk.dto.response.movie.MovieDetailResponse
import chloe.movietalk.dto.response.movie.MovieInfoResponse
import chloe.movietalk.dto.response.movie.UpdateMovieResponse
import chloe.movietalk.service.MovieService
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
@Tag(name = "Movie", description = "Movie APIs - 영화 목록 조회, 생성, 수정, 삭제 및 감독, 배우 목록 갱신 기능 제공")
class MovieControllerImpl : MovieController {
    private val movieService: MovieService? = null

    @GetMapping
    override fun getAllMovies(pageable: Pageable): ResponseEntity<Page<MovieInfoResponse?>?> {
        val movies: Page<MovieInfoResponse?> = movieService!!.getAllMovies(pageable)
        return ResponseEntity.ok().body<Page<MovieInfoResponse?>?>(movies)
    }

    @GetMapping("/{id}")
    override fun getMovieById(id: UUID): ResponseEntity<MovieDetailResponse?> {
        val movie = movieService!!.getMovieById(id)
        return ResponseEntity.ok().body<MovieDetailResponse?>(movie)
    }

    @GetMapping("/search")
    override fun searchMovies(keyword: String, pageable: Pageable): ResponseEntity<Page<MovieInfoResponse?>?> {
        val movies: Page<MovieInfoResponse?> = movieService!!.searchMovies(keyword, pageable)
        return ResponseEntity.ok().body<Page<MovieInfoResponse?>?>(movies)
    }

    @PostMapping
    override fun createMovie(request: MovieRequest): ResponseEntity<MovieInfoResponse?> {
        val movie = movieService!!.createMovie(request)
        return ResponseEntity.status(HttpStatus.CREATED).body<MovieInfoResponse?>(movie)
    }

    @PutMapping("/{id}")
    override fun updateMovie(id: UUID, request: MovieRequest): ResponseEntity<MovieInfoResponse?> {
        val movie = movieService!!.updateMovie(id, request)
        return ResponseEntity.ok().body<MovieInfoResponse?>(movie)
    }

    @DeleteMapping("/{id}")
    override fun deleteMovie(id: UUID): ResponseEntity<Void?> {
        movieService!!.deleteMovie(id)
        return ResponseEntity.noContent().build<Void?>()
    }

    @PostMapping("/{id}/actors")
    override fun updateMovieActors(id: UUID, actorIds: MutableList<UUID?>): ResponseEntity<UpdateMovieResponse?> {
        val movie = movieService!!.updateMovieActors(id, actorIds)
        return ResponseEntity.ok().body<UpdateMovieResponse?>(movie)
    }

    @PostMapping("/{id}/director")
    override fun updateMovieDirector(id: UUID, directorId: UUID): ResponseEntity<UpdateMovieResponse?> {
        val movie = movieService!!.updateMovieDirector(id, directorId)
        return ResponseEntity.ok().body<UpdateMovieResponse?>(movie)
    }
}
