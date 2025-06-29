package chloe.movietalk.controller;

import chloe.movietalk.domain.Actor;
import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.Movie;
import chloe.movietalk.domain.enums.Gender;
import chloe.movietalk.dto.request.MovieRequest;
import chloe.movietalk.exception.director.DirectorErrorCode;
import chloe.movietalk.exception.global.GlobalErrorCode;
import chloe.movietalk.exception.movie.MovieErrorCode;
import chloe.movietalk.repository.ActorRepository;
import chloe.movietalk.repository.DirectorRepository;
import chloe.movietalk.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MovieControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    DirectorRepository directorRepository;

    @Test
    @DisplayName("영화 목록 불러오기 : 디폴트 페이지네이션 옵션")
    public void getAllMoviesWithDefaultPagination() throws Exception {
        // given
        int count = 2;
        List<Movie> movies = getMoviesForTest(count);

        // when
        ResultActions resultActions = mvc.perform(get("/api/movies"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content", hasSize(count)))
                .andExpect(jsonPath("data.content[0].title").value(movies.get(0).getTitle()))
                .andExpect(jsonPath("data.content[1].title").value(movies.get(1).getTitle()));
    }

    @Test
    @DisplayName("영화 목록 불러오기 : 페이지네이션 옵션 지정")
    public void getAllMoviesWithSpecificPagination() throws Exception {
        // given
        int count = 2;
        List<Movie> movies = getMoviesForTest(count);
        Pageable pageable = PageRequest.of(0, 1);

        // when
        ResultActions resultActions = mvc.perform(get("/api/movies")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content", hasSize(pageable.getPageSize())))
                .andExpect(jsonPath("data.content[0].title").value(movies.get(0).getTitle()));
    }

    @Test
    @DisplayName("영화 검색 : 아이디")
    public void findMovieById() throws Exception {
        // given
        Movie movie = getMoviesForTest(1).get(0);

        // when
        ResultActions resultActions = mvc.perform(get("/api/movies/{id}", movie.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.title").value(movie.getTitle()))
                .andExpect(jsonPath("data.codeFIMS").value(movie.getCodeFIMS()));
    }

    @Test
    @DisplayName("영화 검색 : 타이틀 키워드")
    public void searchMovies() throws Exception {
        // given
        Movie movie = getMoviesForTest(1).get(0);

        // when
        ResultActions resultActions = mvc.perform(get("/api/movies/search").param("keyword", "테스트"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content", hasSize(1)))
                .andExpect(jsonPath("data.content[0].title").value(movie.getTitle()));
    }

    @Test
    @DisplayName("영화 등록")
    public void createMovie() throws Exception {
        // given
        MovieRequest movie = MovieRequest.builder()
                .title("테스트 영화 제목")
                .codeFIMS("123123")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.title").value(movie.getTitle()))
                .andExpect(jsonPath("data.codeFIMS").value(movie.getCodeFIMS()));
    }

    @Test
    @DisplayName("영화 등록 실패 : 제목 미입력")
    public void createMovieFailure1() throws Exception {
        // given
        MovieRequest movie = MovieRequest.builder()
                .codeFIMS("123123")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("영화 등록 실패 : FIMS 코드 미입력")
    public void createMovieFailure2() throws Exception {
        // given
        MovieRequest movie = MovieRequest.builder()
                .title("테스트 영화 제목")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("영화 등록 실패 : 존재하지 않는 감독")
    public void createMovieFailure3() throws Exception {
        // given
        MovieRequest movie = MovieRequest.builder()
                .title("테스트 영화 제목")
                .codeFIMS("123123")
                .directorId(UUID.randomUUID())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        // then
        DirectorErrorCode errorCode = DirectorErrorCode.DIRECTOR_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("영화 수정")
    public void updateMovie() throws Exception {
        // given
        Movie movie = getMoviesForTest(1).get(0);
        MovieRequest update = MovieRequest.builder()
                .title("new title")
                .codeFIMS("222")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/movies/{id}", movie.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.title").value(update.getTitle()))
                .andExpect(jsonPath("data.codeFIMS").value(update.getCodeFIMS()));
    }

    @Test
    @DisplayName("영화 수정 실패 : 존재하지 않는 영화")
    public void updateMovieFailure1() throws Exception {
        // given
        MovieRequest update = MovieRequest.builder()
                .title("new title")
                .codeFIMS("222")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/movies/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));

        // then
        MovieErrorCode errorCode = MovieErrorCode.MOVIE_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("영화 삭제")
    public void deleteMovie() throws Exception {
        // given
        Movie movie = getMoviesForTest(1).get(0);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/movies/{id}", movie.getId()));

        // then
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("영화 삭제 실패 : 존재하지 않는 영화")
    public void deleteMovieFailure1() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(delete("/api/movies/{id}", UUID.randomUUID()));

        // then
        MovieErrorCode errorCode = MovieErrorCode.MOVIE_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("영화 배우 목록 업데이트")
    public void updateMovieActors() throws Exception {
        // given
        Movie movie = getMoviesForTest(1).get(0);
        Actor actor = getActorForTest();

        // when
        ResultActions resultActions = mvc.perform(post("/api/movies/{id}/actors", movie.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(actor.getId()))));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.title").value(movie.getTitle()))
                .andExpect(jsonPath("data.codeFIMS").value(movie.getCodeFIMS()))
                .andExpect(jsonPath("data.actors", hasSize(1)))
                .andExpect(jsonPath("data.actors[0].name").value(actor.getName()));
    }

    @Test
    @DisplayName("영화 감독 업데이트")
    public void updateMovieDirector() throws Exception {
        // given
        Movie movie = getMoviesForTest(1).get(0);
        Director director = getDirectorForTest();

        // when
        ResultActions resultActions = mvc.perform(post("/api/movies/{id}/director", movie.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(director.getId())));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.title").value(movie.getTitle()))
                .andExpect(jsonPath("data.codeFIMS").value(movie.getCodeFIMS()))
                .andExpect(jsonPath("data.director").isNotEmpty())
                .andExpect(jsonPath("data.director.name").value(director.getName()));
    }

    private List<Movie> getMoviesForTest(int count) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Movie movie = Movie.builder()
                    .title("테스트용 영화 " + i)
                    .codeFIMS("code" + i)
                    .build();
            movieRepository.save(movie);
            movies.add(movie);
        }
        return movies;
    }

    private Actor getActorForTest() {
        return actorRepository.save(Actor.builder()
                .name("김배우")
                .gender(Gender.MALE)
                .country("대한민국")
                .build());
    }

    private Director getDirectorForTest() {
        return directorRepository.save(Director.builder()
                .name("김감독")
                .gender(Gender.MALE)
                .country("대한민국")
                .build());
    }
}
