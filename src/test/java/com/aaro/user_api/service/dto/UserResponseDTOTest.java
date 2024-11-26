package com.aaro.user_api.service.dto;

import com.aaro.user_api.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

  @Test
  void fromUser() {
    // arrange
    User u = new User("id", "First", "Last", "em@il.com");

    // act
    UserResponseDTO dto = UserResponseDTO.fromUser(u);

    // assert
    assertEquals(u.getId(), dto.getId());
    assertEquals(u.getEmail(), dto.getEmail());
    assertEquals("First Last", dto.getFullName());
  }
}
