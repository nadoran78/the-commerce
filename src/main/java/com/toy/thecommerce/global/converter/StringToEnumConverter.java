package com.toy.thecommerce.global.converter;

import com.toy.thecommerce.domain.user.type.UserListSort;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, UserListSort> {
  @Override
  public UserListSort convert(String source) {
    return UserListSort.find(source);
  }
}
