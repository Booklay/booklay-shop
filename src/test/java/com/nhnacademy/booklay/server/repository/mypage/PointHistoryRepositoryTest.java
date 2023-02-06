package com.nhnacademy.booklay.server.repository.mypage;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import java.util.Optional;
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
import org.springframework.test.util.ReflectionTestUtils;

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
    @DisplayName("retrievePointHistoryByMemberNo 성공 테스트")
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
    @DisplayName("retrieveLatestPointHistory 성공 테스트")
    void retrieveLatestPointHistory_successTest() {
        //given
        pointHistoryRepository.save(pointHistory);

        //when
        Optional<TotalPointRetrieveResponse> totalPointRetrieveResponse =
            pointHistoryRepository.retrieveLatestPointHistory(
                pointHistory.getMember().getMemberNo());

        TotalPointRetrieveResponse expected = totalPointRetrieveResponse.get();

        //then
        assertThat(expected.getTotalPoint()).isEqualTo(pointHistory.getTotalPoint());
    }

    @Test
    @DisplayName("deleteAllByMember_MemberNo success test")
    void deleteAllByMember_MemberNoSuccessTest() {
        //given
        PointHistory point1 = pointHistory;
        PointHistory point2 = pointHistory;
        PointHistory point3 = pointHistory;
        PointHistory point4 = pointHistory;

        ReflectionTestUtils.setField(point1, "id", 1L);
        ReflectionTestUtils.setField(point2, "id", 2L);
        ReflectionTestUtils.setField(point3, "id", 3L);
        ReflectionTestUtils.setField(point4, "id", 4L);

        pointHistoryRepository.save(point1);
        pointHistoryRepository.save(point2);
        pointHistoryRepository.save(point3);
        pointHistoryRepository.save(point4);

        //when
        pointHistoryRepository.deleteAllByMember_MemberNo(point1.getMember().getMemberNo());
        Page<PointHistoryRetrieveResponse> response =
            pointHistoryRepository.retrievePointHistoryByMemberNo(point1.getMember().getMemberNo(),
                PageRequest.of(0, 1));

        assertThat(response.getContent().size()).isZero();
    }
}