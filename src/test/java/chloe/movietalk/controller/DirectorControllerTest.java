package chloe.movietalk.controller;

import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.Movie;
import chloe.movietalk.domain.enums.Gender;
import chloe.movietalk.dto.request.DirectorRequest;
import chloe.movietalk.exception.director.DirectorErrorCode;
import chloe.movietalk.exception.global.GlobalErrorCode;
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
public class DirectorControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    MovieRepository movieRepository;

    @Test
    @DisplayName("감독 목록 불러오기 : 디폴트 페이지네이션 옵션")
    public void getAllDirectorsWithDefaultPagination() throws Exception {
        // given
        int count = 2;
        List<Director> directors = getDirectorsForTest(count);

        // when
        ResultActions resultActions = mvc.perform(get("/api/directors"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content", hasSize(count)))
                .andExpect(jsonPath("data.content[0].name").value(directors.get(0).getName()))
                .andExpect(jsonPath("data.content[1].name").value(directors.get(1).getName()));
    }

    @Test
    @DisplayName("감독 목록 불러오기 : 페이지네이션 옵션 지정")
    public void getAllDirectorsWithSpecificPagination() throws Exception {
        // given
        int count = 2;
        List<Director> directors = getDirectorsForTest(count);
        Pageable pageable = PageRequest.of(0, 1);

        // when
        ResultActions resultActions = mvc.perform(get("/api/directors")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content", hasSize(pageable.getPageSize())))
                .andExpect(jsonPath("data.content[0].name").value(directors.get(0).getName()));
    }

    @Test
    @DisplayName("감독 상세 정보")
    public void findDirectorById() throws Exception {
        // given
        Director director = getDirectorsForTest(1).get(0);
        Movie movie = getMovieForTest(director);

        movie.changeDirector(director);

        // when
        ResultActions resultActions = mvc.perform(get("/api/directors/{id}", director.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.name").value(director.getName()))
                .andExpect(jsonPath("data.gender").value(director.getGender().toString()))
                .andExpect(jsonPath("data.country").value(director.getCountry()))
                .andExpect(jsonPath("data.filmography[0].title").value(movie.getTitle()));
    }

    @Test
    @DisplayName("감독 검색 : 이름 키워드")
    public void searchDirectors() throws Exception {
        // given
        Director director = getDirectorsForTest(1).get(0);

        // when
        ResultActions resultActions = mvc.perform(get("/api/directors/search").param("keyword", "감독"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content", hasSize(1)))
                .andExpect(jsonPath("data.content[0].name").value(director.getName()));
    }

    @Test
    @DisplayName("감독 등록")
    public void createDirector() throws Exception {
        // given
        DirectorRequest director = DirectorRequest.builder()
                .name("김감독")
                .gender("MALE")
                .country("대한민국")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/directors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(director)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.name").value(director.getName()))
                .andExpect(jsonPath("data.gender").value(director.getGender()))
                .andExpect(jsonPath("data.country").value(director.getCountry()));
    }

    @Test
    @DisplayName("감독 등록 실패 : 이름 미입력")
    public void createDirectorFailure1() throws Exception {
        // given
        DirectorRequest director = DirectorRequest.builder()
                .gender("MALE")
                .country("대한민국")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/directors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(director)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("감독 등록 실패 : 잘못된 성별 값")
    public void createDirectorFailure2() throws Exception {
        // given
        DirectorRequest director = DirectorRequest.builder()
                .name("김감독")
                .gender("WRONG")
                .country("대한민국")
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/directors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(director)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_ENUM_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("감독 수정")
    public void updateDirector() throws Exception {
        // given
        Director director = getDirectorsForTest(1).get(0);
        DirectorRequest update = DirectorRequest.builder()
                .name("이감독")
                .gender("FEMALE")
                .country("일본")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/directors/{id}", director.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.name").value(update.getName()))
                .andExpect(jsonPath("data.gender").value(update.getGender()))
                .andExpect(jsonPath("data.country").value(update.getCountry()));
    }

    @Test
    @DisplayName("감독 수정 실패 : 존재하지 않는 감독")
    public void updateDirectorFailure1() throws Exception {
        // given
        DirectorRequest update = DirectorRequest.builder()
                .name("이감독")
                .gender("FEMALE")
                .country("일본")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/directors/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));

        // then
        DirectorErrorCode errorCode = DirectorErrorCode.DIRECTOR_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("감독 삭제")
    public void deleteMovie() throws Exception {
        // given
        Director director = getDirectorsForTest(1).get(0);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/directors/{id}", director.getId()));

        // then
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("감독 삭제 실패 : 존재하지 않는 감독")
    public void deleteDirectorFailure1() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(delete("/api/directors/{id}", UUID.randomUUID()));

        // then
        DirectorErrorCode errorCode = DirectorErrorCode.DIRECTOR_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("감독 필모그라피 업데이트")
    public void updateDirectorFilmography() throws Exception {
        // given
        Director director = getDirectorsForTest(1).get(0);
        Movie movie = getMovieForTest(null);

        // when
        ResultActions resultActions = mvc.perform(post("/api/directors/{id}/filmography", director.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(movie.getId()))));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.name").value(director.getName()))
                .andExpect(jsonPath("data.gender").value(director.getGender().toString()))
                .andExpect(jsonPath("data.country").value(director.getCountry()))
                .andExpect(jsonPath("data.filmography", hasSize(1)))
                .andExpect(jsonPath("data.filmography[0].title").value(movie.getTitle()));
    }

    private List<Director> getDirectorsForTest(int count) {
        List<Director> directors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Director director = Director.builder()
                    .name("감독 " + i)
                    .gender(Gender.MALE)
                    .country("대한민국")
                    .build();
            directorRepository.save(director);
            directors.add(director);
        }
        return directors;
    }

    private Movie getMovieForTest(Director director) {
        return movieRepository.save(Movie.builder()
                .title("테스트용 영화")
                .codeFIMS("123123")
                .director(director)
                .build());
    }
}
