package chloe.movietalk.controller;

import chloe.movietalk.dto.request.CreateReviewRequest;
import chloe.movietalk.dto.request.UpdateReviewRequest;
import chloe.movietalk.dto.response.review.ReviewByMovieResponse;
import chloe.movietalk.dto.response.review.ReviewByUserResponse;
import chloe.movietalk.dto.response.review.ReviewDetailResponse;
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

import java.util.UUID;

public interface ReviewController {

    @PostMapping
    @Operation(summary = "Create new review", description = "새로운 리뷰를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",
                    content = {
                            @Content(schema = @Schema(implementation = ReviewDetailResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 영화가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<ReviewDetailResponse> createReview(
            @Schema(implementation = CreateReviewRequest.class)
            @RequestBody @Valid CreateReviewRequest request
    );

    @PutMapping("/{id}")
    @Operation(summary = "Update review by ID", description = "리뷰 ID로 기존 리뷰 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(schema = @Schema(implementation = ReviewDetailResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 리뷰가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<ReviewDetailResponse> updateReview(
            @Parameter(name = "id", description = "리뷰 ID", required = true)
            @PathVariable UUID id,

            @Schema(implementation = UpdateReviewRequest.class)
            @RequestBody @Valid UpdateReviewRequest request
    );

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review by ID", description = "리뷰 ID로 기존 리뷰 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공",
                    content = {}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 리뷰가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(name = "id", description = "리뷰 ID", required = true)
            @PathVariable UUID id
    );

    @GetMapping("/movies/{id}")
    @Operation(summary = "Get all review lists list by movie id", description = "영화 ID가 일치하는 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewByMovieResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 영화가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Page<ReviewByMovieResponse>> getAllReviewsByMovie(
            @Parameter(name = "id", description = "영화 ID", required = true)
            @PathVariable UUID id,

            @Parameter(name = "pageable", description = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    );

    @GetMapping("/users/{id}")
    @Operation(summary = "Get all review lists list by user id", description = "사용자 ID가 일치하는 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewByMovieResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 사용자가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Page<ReviewByUserResponse>> getAllReviewsByUser(
            @Parameter(name = "id", description = "사용자 ID", required = true)
            @PathVariable UUID id,

            @Parameter(name = "pageable", description = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    );


    @PostMapping("/{id}/like")
    @Operation(summary = "Like review", description = "사용자가 리뷰에 좋아요를 표시합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {}),
            @ApiResponse(responseCode = "400", description = "이미 좋아요 표시된 리뷰입니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "해당 ID의 사용자나 리뷰가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Void> likeReview(
            @Parameter(name = "id", description = "리뷰 ID", required = true)
            @PathVariable UUID id,

            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @RequestParam UUID userId
    );

    @DeleteMapping("/{id}/like")
    @Operation(summary = "Unlike review", description = "사용자가 리뷰 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {}),
            @ApiResponse(responseCode = "404", description = "좋아요 기록이 없거나, 해당 ID의 사용자나 리뷰가 존재하지 않습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<Void> unlikeReview(
            @Parameter(name = "id", description = "리뷰 ID", required = true)
            @PathVariable UUID id,

            @Parameter(name = "userID", description = "사용자 ID", required = true)
            @RequestParam UUID userId
    );
}
