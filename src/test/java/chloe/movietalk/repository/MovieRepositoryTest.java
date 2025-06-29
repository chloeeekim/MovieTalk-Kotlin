package chloe.movietalk.repository;

import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.Movie;
import chloe.movietalk.domain.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Test
    @DisplayName("영화 등록")
    public void createMovie() {
        // given
        Movie movie = Movie.builder()
                .title("테스트용 영화")
                .codeFIMS("123")
                .build();

        // when
        Movie save = movieRepository.save(movie);

        // then
        assertThat(save).isEqualTo(movie);
    }

    @Test
    @DisplayName("영화 목록 불러오기")
    public void movieList() {
        // given
        int count = 30;
        List<Movie> movies = getMoviesForTest(count, null);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<Movie> foundList = movieRepository.findAll(pageable).getContent();

        // then
        assertThat(foundList).hasSize(pageable.getPageSize());
        assertThat(foundList).containsExactlyInAnyOrderElementsOf(movies.subList(0, 10));
    }

    @Test
    @DisplayName("영화 검색 : 아이디")
    public void findById() {
        // given
        Movie movie = getMoviesForTest(1, null).get(0);

        // when
        Movie found = movieRepository.findById(movie.getId()).get();

        // then
        assertThat(found).isEqualTo(movie);
    }

    @Test
    @DisplayName("영화 검색 : 타이틀 키워드")
    public void findByTitle() {
        // given
        Movie movie = getMoviesForTest(1, null).get(0);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        String keyword = "테스트";
        List<Movie> movieList = movieRepository.findByTitleContaining(keyword, pageable).getContent();

        // then
        assertThat(movieList).containsOnly(movie);
    }

    @Test
    @DisplayName("영화 검색 : FIMS 코드")
    public void findByCodeFIMS() {
        // given
        Movie movie = getMoviesForTest(1, null).get(0);

        // when
        Movie found = movieRepository.findByCodeFIMS("code0").get();

        // then
        assertThat(found).isEqualTo(movie);
    }

    @Test
    @DisplayName("영화 검색 : 감독")
    public void findByDirectorId() {
        // given
        Director director = getDirectorForTest();
        Movie movie = getMoviesForTest(1, director).get(0);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<Movie> movieList = movieRepository.findByDirectorId(director.getId(), pageable).getContent();

        // then
        assertThat(movieList).containsOnly(movie);
    }

    private List<Movie> getMoviesForTest(int count, Director director) {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Movie movie = Movie.builder()
                    .title("테스트용 영화 " + i)
                    .codeFIMS("code" + i)
                    .director(director)
                    .build();
            movieRepository.save(movie);
            movies.add(movie);
        }
        return movies;
    }

    private Director getDirectorForTest() {
        return directorRepository.save(Director.builder()
                .name("김감독")
                .gender(Gender.MALE)
                .country("대한민국")
                .build());
    }
}
