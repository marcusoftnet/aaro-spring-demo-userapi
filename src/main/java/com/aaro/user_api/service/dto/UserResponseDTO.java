package com.aaro.user_api.service.dto;

import com.aaro.user_api.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
  private String Id;
  private String fullName;
  private String email;

  public static UserResponseDTO fromUser(User user) {
    return new UserResponseDTO(
      user.getId(),
      user.getFirstName() + " " + user.getLastName(),
      user.getEmail()
    );
  }
}
