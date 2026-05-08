package ie.pt.devopsapplication;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void getAllUsers_shouldReturnUsers() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setActive(true);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setActive(false);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].Id").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].Id").value(2))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].email").value("bob@example.com"))
                .andExpect(jsonPath("$[1].active").value(false));
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setActive(true);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getUserById_whenUserDoesNotExist_shouldReturn404() throws Exception {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addUser_shouldCreateUser() throws Exception {
        User inputUser = new User();
        inputUser.setName("Charlie");
        inputUser.setEmail("charlie@example.com");
        inputUser.setActive(true);

        User savedUser = new User();
        savedUser.setId(3);
        savedUser.setName("Charlie");
        savedUser.setEmail("charlie@example.com");
        savedUser.setActive(true);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.Id").value(3))
                .andExpect(jsonPath("$.name").value("Charlie"))
                .andExpect(jsonPath("$.email").value("charlie@example.com"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void updateUser_whenUserExists_shouldUpdateAndReturnUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setActive(false);

        User updatedDetails = new User();
        updatedDetails.setName("New Name");
        updatedDetails.setEmail("new@example.com");
        updatedDetails.setActive(true);

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(1))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.active").value(true));

        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_whenUserDoesNotExist_shouldReturn404() throws Exception {
        User updatedDetails = new User();
        updatedDetails.setName("New Name");
        updatedDetails.setEmail("new@example.com");
        updatedDetails.setActive(true);

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userRepository).deleteById(1);
    }

    @Test
    void cors_shouldAllowReactDevServerOrigin() throws Exception {
        mockMvc.perform(options("/api/users")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }
}