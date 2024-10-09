package com.itm.space.backendresources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.controller.UserController;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    public void setup() {
        userRequest = new UserRequest("johndoe", "john.doe@example.com", "password", "John", "Doe");
        userResponse = new UserResponse("John", "Doe", "john.doe@example.com", List.of("ROLE_USER"), List.of("Group1"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateUser() throws Exception {
        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    logger.info("Response status: {}", result.getResponse().getStatus());
                });

        logger.info("Finished testCreateUser");
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.getUserById(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userResponse.getLastName()))
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()))
                .andDo(result -> {
                    logger.info("Response body: {}", result.getResponse().getContentAsString());
                });

        logger.info("Finished testGetUserById");
    }

    @Test
    @WithMockUser(roles = "MODERATOR", username = "testuser")
    public void testHello() throws Exception {
        mockMvc.perform(get("/api/users/hello")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("testuser"))
                .andDo(result -> {
                    logger.info("Response body: {}", result.getResponse().getContentAsString());
                });

        logger.info("Finished testHello");
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetUserByIdNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.getUserById(userId)).thenReturn(null);

        mockMvc.perform(get("/api/users/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andDo(result -> {
                    logger.info("Response status: {}", result.getResponse().getStatus());
                });

        logger.info("Finished testGetUserByIdNotFound");
    }


    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateUserValidation() throws Exception {
        UserRequest invalidUserRequest = new UserRequest("", "invalid-email", "password", "John", "Doe");

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserRequest)))
                .andExpect(status().isBadRequest())
                .andDo(result -> {
                    logger.info("Response status: {}", result.getResponse().getStatus());
                });

        logger.info("Finished testCreateUserValidation");
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateUserException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid user data")).when(userService).createUser(any(UserRequest.class));

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid user data"))
                .andDo(result -> {
                    logger.info("Response status: {}", result.getResponse().getStatus());
                    logger.info("Response body: {}", result.getResponse().getContentAsString());
                });

        logger.info("Finished testCreateUserException");
    }
}