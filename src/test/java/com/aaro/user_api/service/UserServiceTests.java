package com.aaro.user_api.service;

import com.aaro.user_api.model.User;
import com.aaro.user_api.model.UserApiResult;
import com.aaro.user_api.repository.UserRepository;
import com.aaro.user_api.service.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

  static class InMemoryUserRepository implements UserRepository {
    private final List<User> users;

    public InMemoryUserRepository(List<User> users) {
      this.users = users;
    }

    @Override
    public List<User> findAll() { return this.users; }

    @Override
    public Optional<User> findById(String id) {
      return this.users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public User create(String firstName, String lastName, String email) {
      User u = new User(UUID.randomUUID().toString(), firstName, lastName, email);
      this.save(u);
      return u;
    }

    @Override
    public void save(User user) { this.users.add(user);  }

    @Override
    public void update(User user) {
      for (int i = 0; i < users.size(); i++) {
        if (users.get(i).getId().equals(user.getId())) {
          users.set(i, user);
          return;
        }
      }
    }

    @Override
    public void deleteById(String id) {
      for (int i = 0; i < users.size(); i++) {
        if (users.get(i).getId().equals(id)) {
          users.remove(i);
          return;
        }
      }
    }
  }

  @Test
  void testGetAllUsers() throws IOException {
    // arrange
    var seedUsers = new ArrayList<User>();
    seedUsers.add(new User("1", "Jane", "Doe", "jane@example.com"));
    seedUsers.add(new User("2", "John", "Doe", "john@example.com"));
    UserRepository repository = new InMemoryUserRepository(seedUsers);
    UserService sut = new UserService(repository);

    // act
    UserApiResult<List<UserResponseDTO>> usersResponse = sut.getAllUsers();

    // assert
    List<UserResponseDTO> users = usersResponse.getData();
    Optional<UserResponseDTO> jane = users.stream().filter(u -> u.getId().equals("1")).findFirst();
    assertEquals(2, users.size());
    assertTrue(jane.isPresent());
    assertEquals("Jane Doe", jane.get().getFullName());
  }

  @Test
  void testGetOneUser() throws IOException {
    // arrange
    var seedUsers = new ArrayList<User>();
    seedUsers.add(new User("1", "Jane", "Doe", "jane@example.com"));
    seedUsers.add(new User("2", "John", "Doe", "john@example.com"));
    UserRepository repository = new InMemoryUserRepository(seedUsers);
    UserService sut = new UserService(repository);

    // Create the request/command to hold parameters
    GetOneUserRequest request = new GetOneUserRequest();
    request.setId("2");

    // act
    UserApiResult<UserResponseDTO> userResponse = sut.getOneUser(request);

    // assert
    UserResponseDTO john = userResponse.getData();
    assertTrue(userResponse.isSuccess());
    assertEquals("John Doe", john.getFullName());
  }

  @Test
  void testGetOneUserNotFound() throws IOException {
    // arrange
    var seedUsers = new ArrayList<User>();
    UserRepository repository = new InMemoryUserRepository(seedUsers);
    UserService sut = new UserService(repository);

    GetOneUserRequest request = new GetOneUserRequest();
    request.setId("2");

    // act
    UserApiResult<UserResponseDTO> userResponse = sut.getOneUser(request);

    // assert
    assertTrue(userResponse.isFailure());
  }

  @Test
  void testCreateUser() throws IOException {
    // arrange
    var seedUsers = new ArrayList<User>();
    UserRepository repository = new InMemoryUserRepository(seedUsers);
    UserService sut = new UserService(repository);

    // Create the request/command to hold parameters
    CreateUserRequest request = new CreateUserRequest();
    request.setFirstName("Marcus");
    request.setLastName("Hammarberg");
    request.setEmail("marcus@marcusoft.net");

    // act
    UserApiResult<UserResponseDTO> createResponse = sut.createUser(request);

    // assert
    UserResponseDTO john = createResponse.getData();
    assertTrue(createResponse.isSuccess());
    assertEquals("Marcus Hammarberg", john.getFullName());
    assertFalse(john.getId().isEmpty());
    assertFalse(john.getId().isBlank());
  }

  @Test
  void testCannotStoreNonValidUserData() throws IOException {
    // arrange
    UserRepository repository = new InMemoryUserRepository(new ArrayList<User>());
    UserService sut = new UserService(repository);

    // Create the request/command to hold parameters
    CreateUserRequest request = new CreateUserRequest();
    request.setFirstName("Marcus");
    request.setLastName("Hammarberg");
    request.setEmail("");

    // act
    UserApiResult<UserResponseDTO> createResponse = sut.createUser(request);

    // assert
    assertTrue(createResponse.isFailure());
    assertFalse(createResponse.getErrorMessage().isEmpty());
    assertTrue(createResponse.getErrorMessage().toLowerCase().contains("email"));
  }

  @Test
  void testDeleteUser() throws IOException {
    // arrange
    var seedUsers = new ArrayList<User>();
    seedUsers.add(new User("1", "Jane", "Doe", "jane@example.com"));
    UserRepository repository = new InMemoryUserRepository(seedUsers);
    UserService sut = new UserService(repository);

    // Create the request/command to hold parameters
    DeleteUserRequest request = new DeleteUserRequest();
    request.setId("1");

    // act
    UserApiResult<String> response = sut.deleteUser(request);

    // assert
    String message = response.getData();
    assertTrue(response.isSuccess());
    assertEquals("Deleted user 1", message);
  }

  @Test
  void testDeleteUserNotFound() throws IOException {
    // arrange
    var seedUsers = new ArrayList<User>();
    UserRepository repository = new InMemoryUserRepository(seedUsers);
    UserService sut = new UserService(repository);

    // Create the request/command to hold parameters
    DeleteUserRequest request = new DeleteUserRequest();
    request.setId("1");

    // act
    UserApiResult<String> response = sut.deleteUser(request);

    // assert
    String message = response.getData();
    assertTrue(response.isFailure());
    assertFalse(response.getErrorMessage().isEmpty());
    assertTrue(response.getErrorMessage().toLowerCase().contains("1"));
  }

  @Test
  void testUpdateUser() throws IOException {
    // arrange
    var seedUsers = new ArrayList<User>();
    seedUsers.add(new User("1", "Jane", "Doe", "jane@example.com"));
    UserRepository repository = new InMemoryUserRepository(seedUsers);
    UserService sut = new UserService(repository);

    UpdateUserRequest request = new UpdateUserRequest();
    request.setId("1");
    request.setFirstName("Jane II");
    request.setLastName("Doe II");
    request.setEmail("jane_ii@example.com");

    // act
    UserApiResult<UserResponseDTO> response = sut.updateUser(request);

    // assert
    UserResponseDTO john = response.getData();
    assertTrue(response.isSuccess());
    assertEquals("Jane II Doe II", john.getFullName());
    assertEquals("jane_ii@example.com", john.getEmail());
  }
}
