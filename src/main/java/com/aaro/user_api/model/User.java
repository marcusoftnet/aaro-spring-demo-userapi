package com.aaro.user_api.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
  private String id;
  private String firstName;
  private String lastName;
  private String email;

  public UserApiResult<User> Validate() {
    if (this.firstName == null || this.firstName.isBlank())
      return UserApiResult.failure("First name must not be blank");

    if (this.lastName == null || this.lastName.isBlank())
      return UserApiResult.failure("Last name must not be blank");

    if (this.email == null || this.email.isBlank())
      return UserApiResult.failure("Email must not be blank");

    return UserApiResult.success(this);
  }
}
