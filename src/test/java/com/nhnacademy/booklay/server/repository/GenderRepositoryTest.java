package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class GenderRepositoryTest {
    @Autowired
    TestEntityManager entityManager;
    @Autowired
    GenderRepository genderRepository;

    @BeforeEach
    public void setup() {
        this.genderRepository.deleteAll();
    }

    @Test
    @DisplayName("GenderRepository save 테스트")
    void testGenderSave() {
        //given
        Gender gender = Dummy.getDummyGender();

        //when
        Gender expected = genderRepository.save(gender);

        //then
        assertThat(expected.getName()).isEqualTo(gender.getName());
    }

    @Test
    @DisplayName("GenderRepository findByName 테스트")
    void testGenderFindByName() {
        //given
        Gender gender = Dummy.getDummyGender();
        genderRepository.save(gender);

        //when
        Gender expected = genderRepository.findByName(gender.getName())
            .orElseThrow(() -> new IllegalArgumentException("Gender not found"));

        //then
        assertThat(expected.getId()).isEqualTo(gender.getId());
    }
}