package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Post;

import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        clearRepo("post", postRepository);
        clearRepo("member", memberRepository);
    }


    @Test
    void testPostSave() {
        Post post = DummyCart.getDummyPost();
        entityManager.persist(post.getMemberId().getGender());
        memberRepository.save(post.getMemberId());


        entityManager.persist(post.getPostTypeId());

        Post expect = postRepository.save(post);

        assertThat(expect.getContent()).isEqualTo(post.getContent());
    }

    @Test
    void testPostFind() {
        Post post = DummyCart.getDummyPost();
        entityManager.persist(post.getMemberId().getGender());
        memberRepository.save(post.getMemberId());


        entityManager.persist(post.getPostTypeId());

        postRepository.save(post);

        Post expect =
            postRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("no"));

        assertThat(expect.getContent()).isEqualTo(post.getContent());
    }

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

}
