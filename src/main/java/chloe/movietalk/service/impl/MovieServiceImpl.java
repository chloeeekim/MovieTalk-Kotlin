package chloe.movietalk.service.impl;

import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.Movie;
import chloe.movietalk.domain.Review;
import chloe.movietalk.dto.request.MovieRequest;
import chloe.movietalk.dto.response.movie.MovieDetailResponse;
import chloe.movietalk.dto.response.movie.MovieInfoResponse;
import chloe.movietalk.dto.response.movie.UpdateMovieResponse;
import chloe.movietalk.exception.actor.ActorNotFoundException;
import chloe.movietalk.exception.director.DirectorNotFoundException;
import chloe.movietalk.exception.movie.AlreadyExistsMovieException;
import chloe.movietalk.exception.movie.MovieNotFoundException;
import chloe.movietalk.repository.ActorRepository;
import chloe.movietalk.repository.DirectorRepository;
import chloe.movietalk.repository.MovieRepository;
import chloe.movietalk.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;

    @Override
    public Page<MovieInfoResponse> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(MovieInfoResponse::fromEntity);
    }

    @Override
    public MovieDetailResponse getMovieById(UUID id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> MovieNotFoundException.EXCEPTION);

        return MovieDetailResponse.fromEntity(movie, getTop3Review(movie.getReviews()));
    }

    @Override
    public Page<MovieInfoResponse> searchMovies(String keyword, Pageable pageable) {
        return movieRepository.findByTitleContaining(keyword, pageable)
                .map(MovieInfoResponse::fromEntity);
    }

    @Override
    public MovieInfoResponse createMovie(MovieRequest request) {
        movieRepository.findByCodeFIMS(request.getCodeFIMS())
                .ifPresent(a -> {
                    throw AlreadyExistsMovieException.EXCEPTION;
                });

        Movie save = movieRepository.save(request.toEntity(getDirectorInfo(request.getDirectorId())));
        return MovieInfoResponse.fromEntity(save);
    }

    @Override
    public MovieInfoResponse updateMovie(UUID id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> MovieNotFoundException.EXCEPTION);

        movie.updateMovie(request.toEntity(getDirectorInfo(request.getDirectorId())));
        return MovieInfoResponse.fromEntity(movie);
    }

    @Override
    public void deleteMovie(UUID id) {
        movieRepository.findById(id)
                .orElseThrow(() -> MovieNotFoundException.EXCEPTION);
        movieRepository.deleteById(id);
    }

    @Override
    public UpdateMovieResponse updateMovieActors(UUID id, List<UUID> actorIds) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> MovieNotFoundException.EXCEPTION);

        movie.getMovieActors().clear();

        actorIds.stream()
                .map(l -> actorRepository.findById(l).orElseThrow(() -> ActorNotFoundException.EXCEPTION))
                .forEach(movie::addActor);

        return UpdateMovieResponse.fromEntity(movie);
    }

    @Override
    public UpdateMovieResponse updateMovieDirector(UUID id, UUID directorId) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> MovieNotFoundException.EXCEPTION);

        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> DirectorNotFoundException.EXCEPTION);

        movie.changeDirector(director);
        return UpdateMovieResponse.fromEntity(movie);
    }

    private Director getDirectorInfo(UUID id) {
        if (id == null) {
            return null;
        } else {
            return directorRepository.findById(id)
                    .orElseThrow(() -> DirectorNotFoundException.EXCEPTION);
        }
    }

    private List<Review> getTop3Review(List<Review> reviews) {
        return reviews.stream()
                .sorted(Comparator.comparingInt(Review::getLikes).reversed())
                .limit(3)
                .toList();
    }
}
