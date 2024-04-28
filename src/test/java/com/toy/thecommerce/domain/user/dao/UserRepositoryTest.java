package com.toy.thecommerce.domain.user.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.toy.thecommerce.domain.user.entity.User;
import com.toy.thecommerce.global.config.JpaAuditConfig;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
@Import(JpaAuditConfig.class)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void saveTestUser() {
    for (int i = 0; i < 10; i++) {
      User user = User.builder()
          .userId("userId" + i)
          .password("password" + i)
          .username(String.valueOf((char) (i + 'a')))
          .nickname("nickname" + i)
          .phone("phone" + i)
          .email("email" + i)
          .build();
      testEntityManager.persist(user);
    }
  }

  @Test
  void successFindAll_whenPageRequestByCreatedAt() {
    //given
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "createdAt");
    //when
    Page<User> pageResult = userRepository.findAll(pageRequest);

    //then
    List<User> resultList = pageResult.getContent();

    assertEquals(10, resultList.get(0).getId());
    assertEquals(9, resultList.get(1).getId());
    assertEquals(8, resultList.get(2).getId());
    assertEquals(7, resultList.get(3).getId());
    assertEquals(6, resultList.get(4).getId());
    assertEquals(5, resultList.get(5).getId());
    assertEquals(4, resultList.get(6).getId());
    assertEquals(3, resultList.get(7).getId());
    assertEquals(2, resultList.get(8).getId());
    assertEquals(1, resultList.get(9).getId());

  }

  @Test
  void successFindAll_whenPageRequestByUsername() {
    //given
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.ASC, "username");
    //when
    Page<User> pageResult = userRepository.findAll(pageRequest);

    //then
    List<User> resultList = pageResult.getContent();

    assertEquals("a", resultList.get(0).getUsername());
    assertEquals("b", resultList.get(1).getUsername());
    assertEquals("c", resultList.get(2).getUsername());
    assertEquals("d", resultList.get(3).getUsername());
    assertEquals("e", resultList.get(4).getUsername());
    assertEquals("f", resultList.get(5).getUsername());
    assertEquals("g", resultList.get(6).getUsername());
    assertEquals("h", resultList.get(7).getUsername());
    assertEquals("i", resultList.get(8).getUsername());
    assertEquals("j", resultList.get(9).getUsername());

  }

}