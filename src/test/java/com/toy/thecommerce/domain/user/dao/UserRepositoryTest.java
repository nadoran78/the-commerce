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
  void saveTestUser() throws InterruptedException {
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
      Thread.sleep(1);
    }
  }

  @Test
  void successFindAll_whenPageRequestByCreatedAt() {
    //given
    PageRequest pageRequest = PageRequest.of(0, 5, Direction.DESC, "createdAt");
    //when
    Page<User> pageResult = userRepository.findAll(pageRequest);

    //then
    List<User> resultList = pageResult.getContent();

    assertEquals(10, resultList.get(0).getId());
    assertEquals(9, resultList.get(1).getId());
    assertEquals(8, resultList.get(2).getId());
    assertEquals(7, resultList.get(3).getId());
    assertEquals(7, resultList.get(4).getId());


  }

  @Test
  void successFindAll_whenPageRequestByUsername() {
    //given
    PageRequest pageRequest = PageRequest.of(1, 5, Direction.ASC, "username");
    //when
    Page<User> pageResult = userRepository.findAll(pageRequest);

    //then
    List<User> resultList = pageResult.getContent();

    assertEquals("f", resultList.get(0).getUsername());
    assertEquals("g", resultList.get(1).getUsername());
    assertEquals("h", resultList.get(2).getUsername());
    assertEquals("i", resultList.get(3).getUsername());
    assertEquals("j", resultList.get(4).getUsername());

  }

}