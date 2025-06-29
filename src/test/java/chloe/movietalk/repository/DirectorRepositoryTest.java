package chloe.movietalk.repository;

import chloe.movietalk.domain.Director;
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
public class DirectorRepositoryTest {

    @Autowired
    DirectorRepository directorRepository;

    @Test
    @DisplayName("감독 등록")
    public void createDirector() {
        // given
        Director director = Director.builder()
                .name("김감독")
                .gender(Gender.MALE)
                .country("대한민국")
                .build();

        // when
        Director save = directorRepository.save(director);

        // then
        assertThat(save).isEqualTo(director);
    }

    @Test
    @DisplayName("감독 목록 불러오기")
    public void directorList() {
        // given
        int count = 30;
        List<Director> directors = getDirectorsForTest(count);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<Director> foundList = directorRepository.findAll(pageable).getContent();

        // then
        assertThat(foundList).hasSize(pageable.getPageSize());
        assertThat(foundList).containsExactlyInAnyOrderElementsOf(directors.subList(0, 10));
    }

    @Test
    @DisplayName("감독 검색 : 아이디")
    public void findById() {
        // given
        Director director = getDirectorsForTest(1).get(0);

        // when
        Director found = directorRepository.findById(director.getId()).get();

        // then
        assertThat(found).isEqualTo(director);
    }

    @Test
    @DisplayName("감독 검색 : 이름")
    public void findByName() {
        // given
        Director director = getDirectorsForTest(1).get(0);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        String keyword = "감독";
        List<Director> directorList = directorRepository.findByNameContaining(keyword, pageable).getContent();

        // then
        assertThat(directorList).containsOnly(director);
    }

    private List<Director> getDirectorsForTest(int count) {
        List<Director> directors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Director director = Director.builder()
                    .name("감독" + i)
                    .gender(Gender.MALE)
                    .country("대한민국")
                    .build();
            directorRepository.save(director);
            directors.add(director);
        }
        return directors;
    }
}
