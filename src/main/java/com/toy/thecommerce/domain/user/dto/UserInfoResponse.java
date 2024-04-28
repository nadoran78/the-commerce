package com.toy.thecommerce.domain.user.dto;

import com.toy.thecommerce.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private String userId;

  private String password;

  private String username;

  private String nickname;

  private String phone;

  private String email;

  public static UserInfoResponse fromEntity(User user, String maskingPassword) {
    return UserInfoResponse.builder()
        .userId(user.getUserId())
        .password(maskingPassword)
        .username(user.getUsername())
        .nickname(user.getNickname())
        .phone(user.getPhone())
        .email(user.getEmail())
        .build();
  }

}
