package org.example.swtest_day3_practice.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void ID로_유저를_조회한다() throws Exception {
        // Given
        User user = new User("김철수", "kim@example.com");
        when(userService.findById(1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void 유저를_생성한다() throws Exception {
        // Given
        User input = new User("이영희", "lee@example.com");
        when(userService.register(any(User.class))).thenReturn(input);

        // When & Then
        mockMvc.perform(
                    post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("이영희"));
    }

    @Test
    void 존재하지_않는_유저_조회시_404() throws Exception {
        // Given
        when(userService.findById(1L)).thenThrow(new IllegalArgumentException("존재하지 않는 유저입니다."));

        // When & Then
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }
}