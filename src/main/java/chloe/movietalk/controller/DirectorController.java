package chloe.movietalk.controller;

import chloe.movietalk.dto.request.DirectorRequest;
import chloe.movietalk.dto.response.director.DirectorDetailResponse;
import chloe.movietalk.dto.response.director.DirectorInfoResponse;
import chloe.movietalk.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface DirectorController {

    @GetMapping
    @Operation(summary = "Get all directors list", description = "모든 감독의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = DirectorInfoResponse.class)))})
    })
    public ResponseEntity<Page<DirectorInfoResponse>> getAllDirectors(
            @Parameter(name = "pageable", description = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    );

    @GetMapping("/{id}")
    @Operation(summary = "Get director by ID", description = "감독 ID로 특정 감독의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(schema = @Schema(implementation = DirectorDetailResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 감독이 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<DirectorDetailResponse> getDirectorById(
            @Parameter(name = "id", description = "감독 ID", required = true)
            @PathVariable UUID id
    );

    @GetMapping("/search")
    @Operation(summary = "Search directors by keyword", description = "감독 이름에 키워드가 포함된 감독 목록을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = DirectorInfoResponse.class)))})
    })
    public ResponseEntity<Page<DirectorInfoResponse>> searchDirectors(
            @Parameter(name = "keyword", description = "검색할 키워드")
            @RequestParam String keyword,

            @Parameter(name = "pageable", description = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    );

    @PostMapping
    @Operation(summary = "Create new director", description = "새로운 감독을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",
                    content = {
                            @Content(schema = @Schema(implementation = DirectorInfoResponse.class))})
    })
    public ResponseEntity<DirectorInfoResponse> createDirector(
            @Schema(implementation = DirectorRequest.class)
            @RequestBody @Valid DirectorRequest request
    );

    @PutMapping("/{id}")
    @Operation(summary = "Update director by ID", description = "감독 ID로 기존 감독 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(schema = @Schema(implementation = DirectorInfoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 감독이 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<DirectorInfoResponse> updateDirector(
            @Parameter(name = "id", description = "감독 ID", required = true)
            @PathVariable UUID id,

            @Schema(implementation = DirectorRequest.class)
            @RequestBody @Valid DirectorRequest request
    );

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete director by ID", description = "감독 ID로 기존 감독 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공",
                    content = {}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 감독이 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Void> deleteDirector(
            @Parameter(name = "id", description = "감독 ID", required = true)
            @PathVariable UUID id
    );

    @PostMapping("/{id}/filmography")
    @Operation(summary = "Update director's filmography by ID", description = "감독 ID로 해당 감독의 필모그라피를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(schema = @Schema(implementation = DirectorDetailResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 감독 혹은 영화가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<DirectorDetailResponse> updateDirectorFilmography(
            @Parameter(name = "id", description = "감독 ID", required = true)
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "새로운 필모그라피로 설정할 영화 ID 리스트",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class)))
            )
            @RequestBody List<UUID> filmography
    );
}
