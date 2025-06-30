package chloe.movietalk.controller

import chloe.movietalk.dto.request.DirectorRequest
import chloe.movietalk.dto.response.director.DirectorDetailResponse
import chloe.movietalk.dto.response.director.DirectorInfoResponse
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

interface DirectorController {
    @GetMapping
    @Operation(summary = "Get all directors list", description = "모든 감독의 목록을 조회합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(array = ArraySchema(schema = Schema(implementation = DirectorInfoResponse::class)))]
        )]
    )
    fun getAllDirectors(
        @Parameter(name = "pageable", description = "페이지네이션 옵션") @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.ASC
        ) pageable: Pageable?
    ): ResponseEntity<Page<DirectorInfoResponse?>?>?

    @GetMapping("/{id}")
    @Operation(summary = "Get director by ID", description = "감독 ID로 특정 감독의 상세 정보를 조회합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = DirectorDetailResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 감독이 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun getDirectorById(
        @Parameter(name = "id", description = "감독 ID", required = true) @PathVariable id: UUID?
    ): ResponseEntity<DirectorDetailResponse?>?

    @GetMapping("/search")
    @Operation(summary = "Search directors by keyword", description = "감독 이름에 키워드가 포함된 감독 목록을 검색합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(array = ArraySchema(schema = Schema(implementation = DirectorInfoResponse::class)))]
        )]
    )
    fun searchDirectors(
        @Parameter(name = "keyword", description = "검색할 키워드") @RequestParam keyword: String?,

        @Parameter(name = "pageable", description = "페이지네이션 옵션") @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.ASC
        ) pageable: Pageable?
    ): ResponseEntity<Page<DirectorInfoResponse?>?>?

    @PostMapping
    @Operation(summary = "Create new director", description = "새로운 감독을 등록합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "성공",
            content = [Content(schema = Schema(implementation = DirectorInfoResponse::class))]
        )]
    )
    fun createDirector(
        @Schema(implementation = DirectorRequest::class) @RequestBody request: @Valid DirectorRequest?
    ): ResponseEntity<DirectorInfoResponse?>?

    @PutMapping("/{id}")
    @Operation(summary = "Update director by ID", description = "감독 ID로 기존 감독 정보를 수정합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = DirectorInfoResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 감독이 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun updateDirector(
        @Parameter(name = "id", description = "감독 ID", required = true) @PathVariable id: UUID?,

        @Schema(implementation = DirectorRequest::class) @RequestBody request: @Valid DirectorRequest?
    ): ResponseEntity<DirectorInfoResponse?>?

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete director by ID", description = "감독 ID로 기존 감독 정보를 삭제합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "204",
            description = "성공",
            content = []
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 감독이 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun deleteDirector(
        @Parameter(name = "id", description = "감독 ID", required = true) @PathVariable id: UUID?
    ): ResponseEntity<Void?>?

    @PostMapping("/{id}/filmography")
    @Operation(summary = "Update director's filmography by ID", description = "감독 ID로 해당 감독의 필모그라피를 수정합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = DirectorDetailResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 감독 혹은 영화가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun updateDirectorFilmography(
        @Parameter(name = "id", description = "감독 ID", required = true) @PathVariable id: UUID?,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "새로운 필모그라피로 설정할 영화 ID 리스트",
            content = [Content(array = ArraySchema(schema = Schema(implementation = Long::class)))]
        ) @RequestBody filmography: MutableList<UUID?>?
    ): ResponseEntity<DirectorDetailResponse?>?
}
