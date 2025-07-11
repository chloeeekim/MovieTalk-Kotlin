package chloe.movietalk.controller

import chloe.movietalk.dto.request.ActorRequest
import chloe.movietalk.dto.response.actor.ActorDetailResponse
import chloe.movietalk.dto.response.actor.ActorInfoResponse
import chloe.movietalk.exception.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

interface ActorController {
    @GetMapping
    @Operation(summary = "Get all actors list", description = "모든 배우의 목록을 조회합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(array = ArraySchema(schema = Schema(implementation = ActorInfoResponse::class)))]
        )]
    )
    fun getAllActors(
        @Parameter(name = "pageable", description = "페이지네이션 옵션")
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<Page<ActorInfoResponse>>

    @GetMapping("/{id}")
    @Operation(summary = "Get actor by ID", description = "배우 ID로 특정 배우의 상세 정보를 조회합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = ActorDetailResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 배우가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun getActorById(
        @Parameter(name = "id", description = "배우 ID", required = true)
        @PathVariable id: UUID
    ): ResponseEntity<ActorDetailResponse>

    @GetMapping("/search")
    @Operation(summary = "Search actors by keyword", description = "배우 이름에 키워드가 포함된 배우 목록을 검색합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(array = ArraySchema(schema = Schema(implementation = ActorInfoResponse::class)))]
        )]
    )
    fun searchActors(
        @Parameter(name = "keyword", description = "검색할 키워드")
        @RequestParam keyword: String,

        @Parameter(name = "pageable", description = "페이지네이션 옵션")
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<Page<ActorInfoResponse>>

    @PostMapping
    @Operation(summary = "Create new actor", description = "새로운 배우를 등록합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "성공",
            content = [Content(schema = Schema(implementation = ActorInfoResponse::class))]
        )]
    )
    fun createActor(
        @Schema(implementation = ActorRequest::class)
        @RequestBody request: @Valid ActorRequest
    ): ResponseEntity<ActorInfoResponse>

    @PutMapping("/{id}")
    @Operation(summary = "Update actor by ID", description = "배우 ID로 기존 배우 정보를 수정합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = ActorInfoResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 배우가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun updateActor(
        @Parameter(name = "id", description = "배우 ID", required = true)
        @PathVariable id: UUID,

        @Schema(implementation = ActorRequest::class)
        @RequestBody request: @Valid ActorRequest
    ): ResponseEntity<ActorInfoResponse>

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete actor by ID", description = "배우 ID로 기존 배우 정보를 삭제합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "204",
            description = "성공",
            content = []
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 배우가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun deleteActor(
        @Parameter(name = "id", description = "배우 ID", required = true)
        @PathVariable id: UUID
    ): ResponseEntity<Void>

    @PostMapping("/{id}/filmography")
    @Operation(summary = "Update actor's filmography by ID", description = "배우 ID로 해당 배우의 필모그라피를 수정합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = ActorDetailResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 배우 혹은 영화가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun updateActorFilmography(
        @Parameter(name = "id", description = "배우 ID", required = true)
        @PathVariable id: UUID,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "새로운 필모그라피로 설정할 영화 ID 리스트",
            content = [Content(array = ArraySchema(schema = Schema(implementation = Long::class)))]
        ) @RequestBody filmography: List<UUID>
    ): ResponseEntity<ActorDetailResponse>
}
