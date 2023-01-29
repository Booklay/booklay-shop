package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.repository.member.BlockedMemberDetailRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author 양승아
 */
@DataJpaTest
@ActiveProfiles("test")
public class BlockedMemberDetailRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BlockedMemberDetailRepository blockedMemberDetailRepository;

    BlockedMemberDetail blockedMemberDetail;

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

        clearRepo("member", memberRepository);
        clearRepo("blocked_member_detail", blockedMemberDetailRepository);

        //given
        blockedMemberDetail = Dummy.getDummyBlockedMemberDetail();

        entityManager.persist(blockedMemberDetail.getMember().getGender());
        memberRepository.save(blockedMemberDetail.getMember());
    }

    @Test
    @DisplayName("DeliveryDetail save test ")
    void testDeliveryDetailSave() {
        //given

        //when
        BlockedMemberDetail expected = blockedMemberDetailRepository.save(blockedMemberDetail);

        //then
        assertThat(expected.getReason()).isEqualTo(blockedMemberDetail.getReason());
    }
}
