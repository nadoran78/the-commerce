package com.toy.thecommerce.domain.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserListSort {
  CREATED_AT("createdAt"),
  USERNAME("username");

  private final String code;

  public static UserListSort find(String source) {
    for (UserListSort value : values()) {
      if (value.getCode().equalsIgnoreCase(source)) {
        return value;
      }
    }
    throw new IllegalArgumentException("정렬 기준이 올바르지 않습니다.");
  }
}
