package chloe.movietalk.dto.response.movie

import chloe.movietalk.domain.Movie
import chloe.movietalk.domain.Review
import chloe.movietalk.dto.response.actor.ActorInfo
import chloe.movietalk.dto.response.director.DirectorInfo
import chloe.movietalk.dto.response.review.ReviewByMovieResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*

data class MovieDetailResponse(
    @field:Schema(description = "영화 ID")
    val id: UUID,
    
    @field:Schema(description = "FIMS 코드")
    val codeFIMS: String,
    
    @field:Schema(description = "영화 제목")
    val title: String,
    
    @field:Schema(description = "시놉시스")
    val synopsis: String,
    
    @field:Schema(description = "개봉일")
    val releaseDate: LocalDate,
    
    @field:Schema(description = "제작연도")
    val prodYear: Int,
    
    @field:Schema(description = "감독") 
    val director: DirectorInfo?,
    
    @field:Schema(description = "배우 목록") 
    val actors: List<ActorInfo>,
    
    @field:Schema(description = "평균 평점")
    val averageRating: Double,
    
    @field:Schema(description = "좋아요 수 기준 상위 리뷰 3개")
    val topReviews: List<ReviewByMovieResponse>
) {
    companion object {
        fun fromEntity(movie: Movie, topReviews: List<Review>): MovieDetailResponse {
            return MovieDetailResponse(
                id = requireNotNull(movie.id) { "Movie ID must not be null"},
                codeFIMS = movie.codeFIMS,
                title = movie.title,
                synopsis = movie.synopsis,
                releaseDate = movie.releaseDate,
                prodYear = movie.prodYear,
                director = movie.director?.let { DirectorInfo.fromEntity(it) },
                actors = movie.getActors().map { ActorInfo.fromEntity(it) },
                averageRating = movie.getAverageRating(),
                topReviews = topReviews.map { ReviewByMovieResponse.fromEntity(it) }
            )
        }
    }
}
