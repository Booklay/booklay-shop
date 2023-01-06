package com.nhnacademy.booklay.server.repository;

import javax.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class ProductAuthorRepositoryTest {

}