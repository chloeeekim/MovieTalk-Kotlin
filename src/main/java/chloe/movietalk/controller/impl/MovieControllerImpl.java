package chloe.movietalk.controller.impl;

import chloe.movietalk.controller.MovieController;
import chloe.movietalk.dto.request.MovieRequest;
import chloe.movietalk.dto.response.movie.MovieDetailResponse;
import chloe.movietalk.dto.response.movie.MovieInfoResponse;
import chloe.movietalk.dto.response.movie.UpdateMovieResponse;
import chloe.movietalk.service.MovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
@Tag(name = "Movie", description = "Movie APIs - 영화 목록 조회, 생성, 수정, 삭제 및 감독, 배우 목록 갱신 기능 제공")
public class MovieControllerImpl implements MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<Page<MovieInfoResponse>> getAllMovies(Pageable pageable) {
        Page<MovieInfoResponse> movies = movieService.getAllMovies(pageable);
        return ResponseEntity.ok().body(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailResponse> getMovieById(UUID id) {
        MovieDetailResponse movie = movieService.getMovieById(id);
        return ResponseEntity.ok().body(movie);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieInfoResponse>> searchMovies(String keyword, Pageable pageable) {
        Page<MovieInfoResponse> movies = movieService.searchMovies(keyword, pageable);
        return ResponseEntity.ok().body(movies);
    }

    @PostMapping
    public ResponseEntity<MovieInfoResponse> createMovie(MovieRequest request) {
        MovieInfoResponse movie = movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieInfoResponse> updateMovie(UUID id, MovieRequest request) {
        MovieInfoResponse movie = movieService.updateMovie(id, request);
        return ResponseEntity.ok().body(movie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(UUID id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/actors")
    public ResponseEntity<UpdateMovieResponse> updateMovieActors(UUID id, List<UUID> actorIds) {
        UpdateMovieResponse movie = movieService.updateMovieActors(id, actorIds);
        return ResponseEntity.ok().body(movie);
    }

    @PostMapping("/{id}/director")
    public ResponseEntity<UpdateMovieResponse> updateMovieDirector(UUID id, UUID directorId) {
        UpdateMovieResponse movie = movieService.updateMovieDirector(id, directorId);
        return ResponseEntity.ok().body(movie);
    }
}
