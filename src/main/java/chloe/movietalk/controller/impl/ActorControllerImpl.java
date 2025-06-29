package chloe.movietalk.controller.impl;

import chloe.movietalk.controller.ActorController;
import chloe.movietalk.dto.request.ActorRequest;
import chloe.movietalk.dto.response.actor.ActorDetailResponse;
import chloe.movietalk.dto.response.actor.ActorInfoResponse;
import chloe.movietalk.service.ActorService;
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
@RequestMapping("/api/actors")
@Tag(name = "Actor", description = "Actor APIs - 배우 목록 조회, 생성, 수정, 삭제 및 필모그라피 갱신 기능 제공")
public class ActorControllerImpl implements ActorController {

    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<Page<ActorInfoResponse>> getAllActors(Pageable pageable) {
        Page<ActorInfoResponse> actors = actorService.getAllActors(pageable);
        return ResponseEntity.ok().body(actors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDetailResponse> getActorById(UUID id) {
        ActorDetailResponse actor = actorService.getActorById(id);
        return ResponseEntity.ok().body(actor);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ActorInfoResponse>> searchActors(String keyword, Pageable pageable) {
        Page<ActorInfoResponse> actors = actorService.searchActor(keyword, pageable);
        return ResponseEntity.ok().body(actors);
    }

    @PostMapping
    public ResponseEntity<ActorInfoResponse> createActor(ActorRequest request) {
        ActorInfoResponse actor = actorService.createActor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(actor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorInfoResponse> updateActor(UUID id, ActorRequest request) {
        ActorInfoResponse actor = actorService.updateActor(id, request);
        return ResponseEntity.ok().body(actor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(UUID id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/filmography")
    public ResponseEntity<ActorDetailResponse> updateActorFilmography(UUID id, List<UUID> filmography) {
        ActorDetailResponse actor = actorService.updateActorFilmography(id, filmography);
        return ResponseEntity.ok().body(actor);
    }
}
