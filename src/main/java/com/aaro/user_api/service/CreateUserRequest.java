package com.aaro.user_api.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateUserRequest {
  private String firstName;
  private String lastName;
  private String email;
}
