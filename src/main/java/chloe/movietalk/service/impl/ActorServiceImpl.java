package chloe.movietalk.service.impl;

import chloe.movietalk.domain.Actor;
import chloe.movietalk.dto.request.ActorRequest;
import chloe.movietalk.dto.response.actor.ActorDetailResponse;
import chloe.movietalk.dto.response.actor.ActorInfoResponse;
import chloe.movietalk.exception.actor.ActorNotFoundException;
import chloe.movietalk.exception.movie.MovieNotFoundException;
import chloe.movietalk.repository.ActorRepository;
import chloe.movietalk.repository.MovieRepository;
import chloe.movietalk.service.ActorService;
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
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    @Override
    public Page<ActorInfoResponse> getAllActors(Pageable pageable) {
        return actorRepository.findAll(pageable)
                .map(ActorInfoResponse::fromEntity);
    }

    @Override
    public ActorDetailResponse getActorById(UUID id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> ActorNotFoundException.EXCEPTION);
        return ActorDetailResponse.fromEntity(actor);
    }

    @Override
    public Page<ActorInfoResponse> searchActor(String keyword, Pageable pageable) {
        return actorRepository.findByNameContaining(keyword, pageable)
                .map(ActorInfoResponse::fromEntity);
    }

    @Override
    public ActorInfoResponse createActor(ActorRequest request) {
        Actor actor = actorRepository.save(request.toEntity());
        return ActorInfoResponse.fromEntity(actor);
    }

    @Override
    public ActorInfoResponse updateActor(UUID id, ActorRequest request) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> ActorNotFoundException.EXCEPTION);

        actor.updateActor(request.toEntity());
        return ActorInfoResponse.fromEntity(actor);
    }

    @Override
    public void deleteActor(UUID id) {
        actorRepository.findById(id)
                .orElseThrow(() -> ActorNotFoundException.EXCEPTION);
        actorRepository.deleteById(id);
    }

    @Override
    public ActorDetailResponse updateActorFilmography(UUID id, List<UUID> filmography) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> ActorNotFoundException.EXCEPTION);

        actor.getMovieActors().clear();

        filmography.stream()
                .map(l -> movieRepository.findById(l).orElseThrow(() -> MovieNotFoundException.EXCEPTION))
                .forEach(actor::addMovie);

        return ActorDetailResponse.fromEntity(actor);
    }
}
