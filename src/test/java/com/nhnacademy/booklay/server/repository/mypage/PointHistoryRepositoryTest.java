package com.nhnacademy.booklay.server.repository.mypage;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.member.reponse.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class PointHistoryRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    MemberRepository memberRepository;

    PointHistory pointHistory;

    void clearRepo(String entityName, JpaRepository jpaRepository) {
        jpaRepository.deleteAll();

        String query =
            String.format("ALTER TABLE `%s` ALTER COLUMN `%s_no` RESTART WITH 1", entityName,
                entityName);

        this.entityManager
            .getEntityManager()
            .createNativeQuery(query)
            .executeUpdate();
    }

    @BeforeEach
    void setUp() {
        clearRepo("point_history", pointHistoryRepository);
        clearRepo("member", memberRepository);

        //given
        pointHistory = Dummy.getDummyPointHistory();

        entityManager.persist(pointHistory.getMember().getGender());
        memberRepository.save(pointHistory.getMember());
    }

    @Test
    @DisplayName("save 테스트")
    void testPointHistoryRepositorySave() {
        //given

        //when
        PointHistory expected = pointHistoryRepository.save(pointHistory);

        //then
        assertThat(expected.getTotalPoint()).isEqualTo(pointHistory.getTotalPoint());

    }

    @Test
    @DisplayName("PointHistoryRepository retrievePointHistoryByMemberNo 성공 테스트")
    void retrievePointHistoryByMemberNo_successTest() {
        //given
        pointHistoryRepository.save(pointHistory);
        pointHistoryRepository.save(new PointHistory(pointHistory.getMember(), 10, 10, "1"));
        pointHistoryRepository.save(new PointHistory(pointHistory.getMember(), 10, 20, "2"));
        pointHistoryRepository.save(new PointHistory(pointHistory.getMember(), 10, 30, "3"));

        PageRequest page = PageRequest.of(0, 3);

        //when
        Page<PointHistoryRetrieveResponse> result =
            pointHistoryRepository.retrievePointHistoryByMemberNo(pointHistory.getMember().getMemberNo(), page);

        //then
        assertThat(result.getSize()).isEqualTo(3);
    }

    @Test
    @DisplayName("PointHistoryRepository retrieveLatestPointHistory 성공 테스트")
    void retrieveLatestPointHistory_successTest() {
        //given
        pointHistoryRepository.save(pointHistory);

        //when
        TotalPointRetrieveResponse expected = pointHistoryRepository.retrieveLatestPointHistory(
                pointHistory.getMember().getMemberNo())
            .orElseThrow(() -> new IllegalArgumentException());

        //then
        assertThat(expected.getTotalPoint()).isEqualTo(pointHistory.getTotalPoint());
    }
}