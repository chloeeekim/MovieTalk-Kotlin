package chloe.movietalk.service.impl;

import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.Movie;
import chloe.movietalk.dto.request.DirectorRequest;
import chloe.movietalk.dto.response.director.DirectorDetailResponse;
import chloe.movietalk.dto.response.director.DirectorInfoResponse;
import chloe.movietalk.exception.director.DirectorNotFoundException;
import chloe.movietalk.exception.movie.MovieNotFoundException;
import chloe.movietalk.repository.DirectorRepository;
import chloe.movietalk.repository.MovieRepository;
import chloe.movietalk.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;

    @Override
    public Page<DirectorInfoResponse> getAllDirectors(Pageable pageable) {
        return directorRepository.findAll(pageable)
                .map(DirectorInfoResponse::fromEntity);
    }

    @Override
    public DirectorDetailResponse getDirectorById(UUID id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> DirectorNotFoundException.EXCEPTION);
        return DirectorDetailResponse.fromEntity(director);
    }

    @Override
    public Page<DirectorInfoResponse> searchDirector(String keyword, Pageable pageable) {
        return directorRepository.findByNameContaining(keyword, pageable)
                .map(DirectorInfoResponse::fromEntity);
    }

    @Override
    public DirectorInfoResponse createDirector(DirectorRequest request) {
        Director save = directorRepository.save(request.toEntity());
        return DirectorInfoResponse.fromEntity(save);
    }

    @Override
    public DirectorInfoResponse updateDirector(UUID id, DirectorRequest request) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> DirectorNotFoundException.EXCEPTION);

        director.updateDirector(request.toEntity());
        return DirectorInfoResponse.fromEntity(director);
    }

    @Override
    public void deleteDirector(UUID id) {
        directorRepository.findById(id)
                .orElseThrow(() -> DirectorNotFoundException.EXCEPTION);
        directorRepository.deleteById(id);
    }

    @Override
    public DirectorDetailResponse updateDirectorFilmography(UUID id, List<UUID> filmography) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> DirectorNotFoundException.EXCEPTION);

        director.getFilmography()
                .forEach(Movie::removeDirector);
        director.getFilmography().clear();

        filmography.stream()
                .map(l -> movieRepository.findById(l).orElseThrow(() -> MovieNotFoundException.EXCEPTION))
                .forEach(m -> m.changeDirector(director));

        return DirectorDetailResponse.fromEntity(director);
    }
}
