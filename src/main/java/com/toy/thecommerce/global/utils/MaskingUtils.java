package com.toy.thecommerce.global.utils;

public class MaskingUtils {

  private static final String MASKING_PASSWORD = "********";

  /**
   * userId, nickname, username 마스킹 적용
   * value 길이가 1 또는 2인 경우 : 첫번째 문자를 * 처리 userId의 길이가 3 또는 4인 경우 :
   * value 길이가 3 또는 4인 경우 : 첫번째, 마지막 문자를 제외하고 * 처리
   * value 길이가 5 이상인 경우 : 첫번째 문자 이후 3개의 문자를 * 처리
   */
  public static String maskingUserIdOrName(String value) {
    String maskingValue;
    if (value.length() == 1 || value.length() == 2) {
      maskingValue = (value.length() == 1) ? "*" : "*" + value.charAt(1);
    } else if (value.length() == 3 || value.length() == 4) {
      maskingValue = (value.length() == 3) ? value.charAt(0) + "*" + value.charAt(2)
          : value.charAt(0) + "**" + value.substring(3);
    } else {
      maskingValue = value.charAt(0) + "***" + value.substring(4);
    }
    return maskingValue;
  }

  /**
   * 이메일 마스킹 적용
   * '@' 이전의 문자열을 userId, nickname, username과 동일하게 처리
   */
  public static String maskingEmail(String email) {
    String[] splitEmail = email.split("@");
    return maskingUserIdOrName(splitEmail[0]) + "@" + splitEmail[1];
  }

  /**
   * 휴대전화번호 마스킹 적용
   * 가운데 전화번호 3~4자리 **** 처리
   */
  public static String maskingPhone(String phone) {
    phone = addHyphen(phone);
    return phone.substring(0, 3) + "-****-" + phone.substring(phone.length() - 4);
  }

  private static String addHyphen (String phone) {
    if (phone.contains("-")) {
      return phone;
    } else {
      return phone.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
    }
  }

  public static String getMaskingPassword() {
    return MASKING_PASSWORD;
  }

}
