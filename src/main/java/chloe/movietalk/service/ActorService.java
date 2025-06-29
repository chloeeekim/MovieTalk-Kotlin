package chloe.movietalk.service;

import chloe.movietalk.dto.request.ActorRequest;
import chloe.movietalk.dto.response.actor.ActorDetailResponse;
import chloe.movietalk.dto.response.actor.ActorInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ActorService {

    public Page<ActorInfoResponse> getAllActors(Pageable pageable);

    public ActorDetailResponse getActorById(UUID id);

    public Page<ActorInfoResponse> searchActor(String keyword, Pageable pageable);

    public ActorInfoResponse createActor(ActorRequest request);

    public ActorInfoResponse updateActor(UUID id, ActorRequest request);

    public void deleteActor(UUID id);

    public ActorDetailResponse updateActorFilmography(UUID id, List<UUID> filmography);
}
