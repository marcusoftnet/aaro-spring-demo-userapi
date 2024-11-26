package com.aaro.user_api.repository;

import com.aaro.user_api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
  List<User> findAll();

  Optional<User> findById(String id);

  User create(String firstName, String lastName, String email);

  void save(User user);

  void update(User user);

  void deleteById(String id);
}
