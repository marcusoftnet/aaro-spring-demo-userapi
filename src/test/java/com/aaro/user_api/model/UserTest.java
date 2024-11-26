package com.aaro.user_api.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

  @Test
  void emptyFirstNameGivesAnErrorMessage() {
    // arrange
    User u = new User("id", "", "Last", "em@il.com");

    // act
    UserApiResult<User> res = u.Validate();

    // assert
    assertTrue(res.isFailure());
    assertFalse(res.isSuccess());
    assertFalse(res.getErrorMessage().isEmpty());
  }

  @Test
  void emptyLastNameGivesAnErrorMessage() {
    // arrange
    User u = new User("id", "First", "", "em@il.com");

    // act
    UserApiResult<User> res = u.Validate();

    // assert
    assertTrue(res.isFailure());
    assertFalse(res.isSuccess());
    assertFalse(res.getErrorMessage().isEmpty());
  }

  @Test
  void emptyEmailGivesAnErrorMessage() {
    // arrange
    User u = new User("id", "First", "Last", "");

    // act
    UserApiResult<User> res = u.Validate();

    // assert
    assertTrue(res.isFailure());
    assertFalse(res.isSuccess());
    assertFalse(res.getErrorMessage().isEmpty());
  }

  @Test
  void validUserDoesNotGivesErrorMessage() {
    // arrange
    User u = new User("id", "First", "Last", "em@il.com");

    // act
    UserApiResult<User> res = u.Validate();

    // assert
    assertFalse(res.isFailure());
    assertTrue(res.isSuccess());
    assertNull(res.getErrorMessage());
    assertEquals(res.getData().getId(), u.getId());
    assertEquals(res.getData().getFirstName(), u.getFirstName());
    assertEquals(res.getData().getLastName(), u.getLastName());
    assertEquals(res.getData().getEmail(), u.getEmail());
  }
}
