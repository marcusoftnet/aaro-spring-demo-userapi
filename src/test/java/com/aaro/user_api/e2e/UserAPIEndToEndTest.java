package com.aaro.user_api.e2e;
import com.aaro.user_api.service.dto.CreateUserRequest;
import com.aaro.user_api.service.dto.UserResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e")
public class UserAPIEndToEndTest {

  @LocalServerPort
  private int port;

  private String baseUrl;
  private RestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/api/v1/users";
    restTemplate = new RestTemplate();
  }

  @AfterEach
  void tearDown() throws IOException {
    resetUserDataFile();
  }

  @Test
  void testGetUser() throws IOException {
    // Arrange
    // Setup the data file in a known state
    setupDataFile("""
      [
        {"id": "1", "firstName": "Jane", "lastName": "Doe", "email":"jane@example.com"}
      ]
      """);

    // Act - GET a user
    String urlWithId = baseUrl + "/1";
    ResponseEntity<UserResponseDTO> getResponse = restTemplate.getForEntity(urlWithId, UserResponseDTO.class);
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());

    UserResponseDTO retrievedUser = getResponse.getBody();
    assertNotNull(retrievedUser);
    assertEquals("Jane Doe", retrievedUser.getFullName());
    assertEquals("jane@example.com", retrievedUser.getEmail());
  }

  @Test
  void testCreateAndRetrieveUser() throws IOException {
    // Arrange
    // Setup the data file in a known state - empty
    resetUserDataFile();

    // Act - POST a new user
    CreateUserRequest request = new CreateUserRequest("Marcus", "Hammarberg", "marcus@marcusoft.net");
    ResponseEntity<UserResponseDTO> createResponse = restTemplate.postForEntity(baseUrl, request, UserResponseDTO.class);

    // Assert - POST
    assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    var createdUser = createResponse.getBody();
    assertNotNull(createdUser.getId());

    // Act - GET created user
    String urlWithId = baseUrl + "/" + createdUser.getId();
    ResponseEntity<UserResponseDTO> getResponse = restTemplate.getForEntity(urlWithId, UserResponseDTO.class);

    // Assert - GET
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    UserResponseDTO retrievedUser = getResponse.getBody();
    assertNotNull(retrievedUser);
    assertEquals(createdUser.getId(), retrievedUser.getId());
    assertEquals("Marcus Hammarberg", retrievedUser.getFullName());
    assertEquals("marcus@marcusoft.net", retrievedUser.getEmail());
  }

  private void resetUserDataFile() throws IOException {
    setupDataFile("[]");
  }

  private void setupDataFile(String json) throws IOException {
    // Construct the absolute path to the user.json file
    File userFile = Paths.get("src", "main", "resources", "users.json").toAbsolutePath().toFile();
    if (userFile.exists()) {
      Files.write(userFile.toPath(), json.getBytes()); // Reset file to an empty array
    } else {
      throw new IOException("user.json file does not exist at " + userFile.getAbsolutePath());
    }
  }
}
