package com.toy.thecommerce.domain.user.entity;

import static lombok.AccessLevel.PROTECTED;

import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.global.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(force = true, access = PROTECTED)
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userId;

  private String password;

  private String nickname;

  private String username;

  private String phone;

  private String email;

  public static User from(SignUpRequest request, String encodedPassword) {
    return User.builder()
        .userId(request.getUserId())
        .password(encodedPassword)
        .nickname(request.getNickname())
        .username(request.getUsername())
        .phone(request.getPhone())
        .email(request.getEmail())
        .build();
  }

}
