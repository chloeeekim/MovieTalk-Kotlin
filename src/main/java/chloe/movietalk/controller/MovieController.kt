package chloe.movietalk.controller

import chloe.movietalk.dto.request.MovieRequest
import chloe.movietalk.dto.response.movie.MovieDetailResponse
import chloe.movietalk.dto.response.movie.MovieInfoResponse
import chloe.movietalk.dto.response.movie.UpdateMovieResponse
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

interface MovieController {
    @GetMapping
    @Operation(summary = "Get all movies list", description = "모든 영화의 목록을 조회합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(array = ArraySchema(schema = Schema(implementation = MovieInfoResponse::class)))]
        )]
    )
    fun getAllMovies(
        @Parameter(name = "pageable", description = "페이지네이션 옵션")
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<Page<MovieInfoResponse>>

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID", description = "영화 ID로 특정 영화의 상세 정보를 조회합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = MovieDetailResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 영화가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun getMovieById(
        @Parameter(name = "id", description = "영화 ID", required = true)
        @PathVariable id: UUID
    ): ResponseEntity<MovieDetailResponse>

    @GetMapping("/search")
    @Operation(summary = "Search movies by keyword", description = "영화 제목에 키워드가 포함된 영화 목록을 검색합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(array = ArraySchema(schema = Schema(implementation = MovieInfoResponse::class)))]
        )]
    )
    fun searchMovies(
        @Parameter(name = "keyword", description = "검색할 키워드")
        @RequestParam keyword: String,

        @Parameter(name = "pageable", description = "페이지네이션 옵션")
        @PageableDefault(page = 0, size = 10, sort = ["createdAt"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<Page<MovieInfoResponse>>

    @PostMapping
    @Operation(summary = "Create new movie", description = "새로운 영화를 등록합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "성공",
            content = [Content(schema = Schema(implementation = MovieInfoResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "이미 존재하는 영화입니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun createMovie(
        @Schema(implementation = MovieRequest::class)
        @RequestBody request: @Valid MovieRequest
    ): ResponseEntity<MovieInfoResponse>

    @PutMapping("/{id}")
    @Operation(summary = "Update movie by ID", description = "영화 ID로 기존 영화 정보를 수정합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = MovieInfoResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 영화가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun updateMovie(
        @Parameter(name = "id", description = "영화 ID", required = true)
        @PathVariable id: UUID,

        @Schema(implementation = MovieRequest::class)
        @RequestBody request: @Valid MovieRequest
    ): ResponseEntity<MovieInfoResponse>

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete movie by ID", description = "영화 ID로 기존 영화 정보를 삭제합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "204",
            description = "성공",
            content = []
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 영화가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun deleteMovie(
        @Parameter(name = "id", description = "영화 ID", required = true)
        @PathVariable id: UUID
    ): ResponseEntity<Void>

    @PostMapping("/{id}/actors")
    @Operation(summary = "Update movie's actors list by ID", description = "영화 ID로 해당 영화의 배우 목록을 수정합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = UpdateMovieResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 영화 혹은 배우가 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun updateMovieActors(
        @Parameter(name = "id", description = "영화 ID", required = true)
        @PathVariable id: UUID,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "새로운 배우 목록으로 설정할 배우 ID 리스트",
            content = [Content(array = ArraySchema(schema = Schema(implementation = Long::class)))]
        ) @RequestBody actorIds: List<UUID>
    ): ResponseEntity<UpdateMovieResponse>

    @PostMapping("/{id}/director")
    @Operation(summary = "Update movie's director by ID", description = "영화 ID로 해당 영화의 감독을 수정합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "성공",
            content = [Content(schema = Schema(implementation = UpdateMovieResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "해당 ID의 영화 혹은 감독이 존재하지 않습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    fun updateMovieDirector(
        @Parameter(name = "id", description = "영화 ID", required = true)
        @PathVariable id: UUID,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "새로운 배우 목록으로 설정할 배우 ID 리스트",
            content = [Content(schema = Schema(implementation = Long::class))]
        ) @RequestBody directorId: UUID
    ): ResponseEntity<UpdateMovieResponse>
}
