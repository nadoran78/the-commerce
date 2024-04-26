package com.toy.thecommerce.domain.user.dao;

import com.toy.thecommerce.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserId(String userId);
  Optional<User> findByPhone(String phone);
}
