package chloe.movietalk.repository;

import chloe.movietalk.domain.Actor;
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
public class ActorRepositoryTest {

    @Autowired
    ActorRepository actorRepository;

    @Test
    @DisplayName("배우 등록")
    public void createActor() {
        // given
        Actor actor = Actor.builder()
                .name("김배우")
                .gender(Gender.MALE)
                .country("대한민국")
                .build();

        // when
        Actor save = actorRepository.save(actor);

        // then
        assertThat(save).isEqualTo(actor);
    }

    @Test
    @DisplayName("배우 목록 불러오기")
    public void actorList() {
        // given
        int count = 30;
        List<Actor> actors = getActorsForTest(count);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<Actor> foundList = actorRepository.findAll(pageable).getContent();

        // then
        assertThat(foundList).hasSize(pageable.getPageSize());
        assertThat(foundList).containsExactlyInAnyOrderElementsOf(actors.subList(0, 10));
    }

    @Test
    @DisplayName("배우 검색 : 아이디")
    public void findById() {
        // given
        Actor actor = getActorsForTest(1).get(0);

        // when
        Actor found = actorRepository.findById(actor.getId()).get();

        // then
        assertThat(found).isEqualTo(actor);
    }

    @Test
    @DisplayName("배우 검색 : 이름")
    public void findByName() {
        // given
        Actor actor = getActorsForTest(1).get(0);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        String keyword = "배우";
        List<Actor> actorList = actorRepository.findByNameContaining(keyword, pageable).getContent();

        // then
        assertThat(actorList).containsOnly(actor);
    }

    private List<Actor> getActorsForTest(int count) {
        List<Actor> actors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Actor actor = Actor.builder()
                    .name("배우" + i)
                    .gender(Gender.MALE)
                    .country("대한민국")
                    .build();
            actorRepository.save(actor);
            actors.add(actor);
        }
        return actors;
    }
}
