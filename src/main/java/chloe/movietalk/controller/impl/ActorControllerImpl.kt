package chloe.movietalk.controller.impl

import chloe.movietalk.controller.ActorController
import chloe.movietalk.dto.request.ActorRequest
import chloe.movietalk.dto.response.actor.ActorDetailResponse
import chloe.movietalk.dto.response.actor.ActorInfoResponse
import chloe.movietalk.service.ActorService
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/actors")
@Tag(name = "Actor", description = "Actor APIs - 배우 목록 조회, 생성, 수정, 삭제 및 필모그라피 갱신 기능 제공")
class ActorControllerImpl(
    private val actorService: ActorService
) : ActorController {

    @GetMapping
    override fun getAllActors(pageable: Pageable): ResponseEntity<Page<ActorInfoResponse>> {
        val actors = actorService.getAllActors(pageable)
        return ResponseEntity.ok(actors)
    }

    @GetMapping("/{id}")
    override fun getActorById(id: UUID): ResponseEntity<ActorDetailResponse> {
        val actor = actorService.getActorById(id)
        return ResponseEntity.ok(actor)
    }

    @GetMapping("/search")
    override fun searchActors(keyword: String, pageable: Pageable): ResponseEntity<Page<ActorInfoResponse>> {
        val actors = actorService.searchActor(keyword, pageable)
        return ResponseEntity.ok(actors)
    }

    @PostMapping
    override fun createActor(request: ActorRequest): ResponseEntity<ActorInfoResponse> {
        val actor = actorService.createActor(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(actor)
    }

    @PutMapping("/{id}")
    override fun updateActor(id: UUID, request: ActorRequest): ResponseEntity<ActorInfoResponse> {
        val actor = actorService.updateActor(id, request)
        return ResponseEntity.ok(actor)
    }

    @DeleteMapping("/{id}")
    override fun deleteActor(id: UUID): ResponseEntity<Void> {
        actorService.deleteActor(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/filmography")
    override fun updateActorFilmography(id: UUID, filmography: List<UUID>): ResponseEntity<ActorDetailResponse> {
        val actor = actorService.updateActorFilmography(id, filmography)
        return ResponseEntity.ok(actor)
    }
}
