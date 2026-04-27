package org.example.swtest_day3_practice;

import org.example.swtest_day3_practice.user.User;
import org.example.swtest_day3_practice.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class SwtestDay3PracticeApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void 유저를_등록한다() {
        // Given
        User user = new User("김철수", "kim@example.com");

        // When
        ResponseEntity<User> response = restTemplate.postForEntity(
                "/users",
                user,
                User.class
        );

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("김영희", response.getBody().getName());
    }

    @Test
    void 저장된_유저를_ID로_조회한다() {
        // Given
        User saved = userRepository.save(new User("이영희", "lee@example.com"));

        // When
        ResponseEntity<User> response = restTemplate.getForEntity("/users/{id}", User.class, saved.getId());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("이영희", response.getBody().getName());
    }

    @Test
    void 존재하지_않는_유저_조회시_404() {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/users/999", String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
