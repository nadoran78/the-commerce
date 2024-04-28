package com.toy.thecommerce.domain.user.dto;

import com.toy.thecommerce.domain.user.entity.User;
import com.toy.thecommerce.global.utils.MaskingUtils;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserListResponse {
  private String userId;

  private String nickname;

  private String username;

  private String phone;

  private String email;

  public static UserListResponse maskingUserListResponse(User user) {
    return UserListResponse.builder()
        .userId(MaskingUtils.maskingUserIdOrName(user.getUserId()))
        .nickname(MaskingUtils.maskingUserIdOrName(user.getNickname()))
        .username(MaskingUtils.maskingUserIdOrName(user.getUsername()))
        .phone(MaskingUtils.maskingPhone(user.getPhone()))
        .email(MaskingUtils.maskingEmail(user.getEmail()))
        .build();
  }
}
