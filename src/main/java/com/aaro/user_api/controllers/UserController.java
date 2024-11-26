package com.aaro.user_api.controllers;

import com.aaro.user_api.model.UserApiResult;
import com.aaro.user_api.service.*;
import com.aaro.user_api.service.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/")
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    var response = this.userService.getAllUsers();
    if (response.isFailure()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(response.getData());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getAllUsers(GetOneUserRequest request) {
    var response = this.userService.getOneUser(request);
    if (response.isFailure()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(response.getData());
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequest request) {
    UserApiResult<UserResponseDTO> response = userService.createUser(request);
    if (response.isFailure()) {
      return ResponseEntity.notFound().build();
    }

    UserResponseDTO user = response.getData();
    String location = getLocationForUser(user);

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .header(HttpHeaders.LOCATION, location)
      .body(user);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
    request.setId(id);
    UserApiResult<UserResponseDTO> response = userService.updateUser(request);
    if (response.isFailure()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity
      .ok(response.getData());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUserById(DeleteUserRequest request) {
    UserApiResult<String> response = userService.deleteUser(request);

    if (response.isFailure()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }

  private static String getLocationForUser(UserResponseDTO user) {
    return ServletUriComponentsBuilder
      .fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(user.getId())
      .toString();
  }
}
