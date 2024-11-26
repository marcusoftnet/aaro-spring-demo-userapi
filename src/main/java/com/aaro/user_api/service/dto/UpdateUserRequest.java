package com.aaro.user_api.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateUserRequest {
  private String id;
  private String firstName;
  private String lastName;
  private String email;
}
