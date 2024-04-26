package com.toy.thecommerce.global.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MaskingUtilsTest {

  @Test
  void maskingUserId_whenInputByKorean() {
    //given
    String one = "아";
    String two = "아아";
    String three = "아아아";
    String four = "아아아아";
    String six = "아아아아아아";
    //when & then
    assertEquals("*", MaskingUtils.maskingUserIdOrName(one));
    assertEquals("*아", MaskingUtils.maskingUserIdOrName(two));
    assertEquals("아*아", MaskingUtils.maskingUserIdOrName(three));
    assertEquals("아**아", MaskingUtils.maskingUserIdOrName(four));
    assertEquals("아***아아", MaskingUtils.maskingUserIdOrName(six));
  }

  @Test
  void maskingUserId_whenInputByEnglish() {
    //given
    String one = "a";
    String two = "ab";
    String three = "abc";
    String four = "abcd";
    String six = "abcdef";
    //when & then
    assertEquals("*", MaskingUtils.maskingUserIdOrName(one));
    assertEquals("*b", MaskingUtils.maskingUserIdOrName(two));
    assertEquals("a*c", MaskingUtils.maskingUserIdOrName(three));
    assertEquals("a**d", MaskingUtils.maskingUserIdOrName(four));
    assertEquals("a***ef", MaskingUtils.maskingUserIdOrName(six));
  }

  @Test
  void maskingEmail() {
    // given
    String email = "abcd@test.com";
    // when & then
    assertEquals("a**d@test.com", MaskingUtils.maskingEmail(email));
  }

  @Test
  void maskingPhoneNumber() {
    // given
    String phone1 = "010-0000-0000";
    String phone2 = "01000000000";

    // when & then
    assertEquals("010-****-0000", MaskingUtils.maskingPhone(phone1));
    assertEquals("010-****-0000", MaskingUtils.maskingPhone(phone2));
  }

}