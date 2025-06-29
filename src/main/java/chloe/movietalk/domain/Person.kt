package chloe.movietalk.domain;

import chloe.movietalk.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Person extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 50)
    private String country;

    public Person(String name, Gender gender, String country) {
        this.name = name;
        this.gender = gender;
        this.country = country;
    }

    public void updatePerson(String name, Gender gender, String country) {
        this.name = name;
        this.gender = gender;
        this.country = country;
    }
}
