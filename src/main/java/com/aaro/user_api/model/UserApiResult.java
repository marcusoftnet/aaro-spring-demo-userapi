package com.aaro.user_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserApiResult<T> {
  private T data;
  private String errorMessage;

  public static <T> UserApiResult<T> success(T data) {
    return new UserApiResult<>(data, null);
  }

  public static <T> UserApiResult<T> failure(String error) {
    return new UserApiResult<>(null, error);
  }

  public boolean isSuccess() {
    return errorMessage == null;
  }

  public boolean isFailure() {
    return errorMessage != null;
  }
}




