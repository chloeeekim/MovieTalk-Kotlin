package chloe.movietalk.service;

import chloe.movietalk.dto.request.MovieRequest;
import chloe.movietalk.dto.response.movie.MovieDetailResponse;
import chloe.movietalk.dto.response.movie.MovieInfoResponse;
import chloe.movietalk.dto.response.movie.UpdateMovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MovieService {
    public Page<MovieInfoResponse> getAllMovies(Pageable pageable);

    public MovieDetailResponse getMovieById(UUID id);

    public Page<MovieInfoResponse> searchMovies(String keyword, Pageable pageable);

    public MovieInfoResponse createMovie(MovieRequest request);

    public MovieInfoResponse updateMovie(UUID id, MovieRequest request);

    public void deleteMovie(UUID id);

    public UpdateMovieResponse updateMovieActors(UUID id, List<UUID> actorIds);

    public UpdateMovieResponse updateMovieDirector(UUID id, UUID directorId);
}
