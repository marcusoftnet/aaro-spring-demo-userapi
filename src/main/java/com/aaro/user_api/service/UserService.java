package com.aaro.user_api.service;

import com.aaro.user_api.model.User;
import com.aaro.user_api.model.UserApiResult;
import com.aaro.user_api.repository.UserRepository;
import com.aaro.user_api.service.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserApiResult<List<UserResponseDTO>> getAllUsers() {
    try {
      List<User> usersFromDatabase = userRepository.findAll();
      List<UserResponseDTO> userDTOs = usersFromDatabase.stream().map(UserResponseDTO::fromUser).toList();
      return UserApiResult.success(userDTOs);
    } catch (Exception e) {
      return UserApiResult.failure("Failed to fetch users: " + e.getMessage());
    }
  }

  public UserApiResult<UserResponseDTO> getOneUser(GetOneUserRequest request) {
    try {
      Optional<User> userFromDatabase = userRepository.findById(request.id());
      if (userFromDatabase.isPresent()) {
        UserResponseDTO userDTOs = UserResponseDTO.fromUser(userFromDatabase.get());
        return UserApiResult.success(userDTOs);
      }
      return UserApiResult.failure("Could not find user with id: " + request.id());
    } catch (Exception e) {
      return UserApiResult.failure("Failed to fetch user: " + e.getMessage());
    }
  }

  public UserApiResult<UserResponseDTO> createUser(CreateUserRequest request) {
    try {
      User createdUser = userRepository.create(request.firstName(), request.lastName(), request.email());

      UserApiResult<User> validationResult = createdUser.Validate();
      if (validationResult.isFailure()) {
        return UserApiResult.failure(validationResult.getErrorMessage());
      }

      UserResponseDTO dto = UserResponseDTO.fromUser(createdUser);
      return UserApiResult.success(dto);
    } catch (Exception e) {
      return UserApiResult.failure(e.getMessage());
    }
  }

  public UserApiResult<UserResponseDTO> updateUser(UpdateUserRequest request) {
    try {
      Optional<User> existing = userRepository.findById(request.getId());
      if (existing.isEmpty()) {
        return UserApiResult.failure("User not found with ID: " + request.getId());
      }

      User user = new User(request.getId(), request.getFirstName(), request.getLastName(), request.getEmail());
      UserApiResult<User> validationResult = user.Validate();
      if (validationResult.isFailure()) {
        return UserApiResult.failure(validationResult.getErrorMessage());
      }

      userRepository.update(user);
      return UserApiResult.success(UserResponseDTO.fromUser(user));
    } catch (Exception e) {
      return UserApiResult.failure(e.getMessage());
    }
  }

  public UserApiResult<String> deleteUser(DeleteUserRequest request) {
    try {
      Optional<User> existing = userRepository.findById(request.id());
      if (existing.isEmpty()) {
        return UserApiResult.failure("User not found with ID: " + request.id());
      }

      userRepository.deleteById(request.id());

      return UserApiResult.success("Deleted user " + request.id());
    } catch (Exception e) {
      return UserApiResult.failure(e.getMessage());
    }
  }
}

