package ru.effectivemobile.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.effectivemobile.dto.account.RegisterRequest;
import ru.effectivemobile.dto.auth.AuthRequest;
import ru.effectivemobile.repository.UserRepository;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    private static final String REGISTER_ENDPOINT = "/v1/account/register";
    private static final String AUTH_ENDPOINT = "/v1/auth/login";
    private static final String LOGIN = "testLogin";
    private static final String PASSWORD = "testPassword";
    private static final String EMAIL = "test@mail.ru";
    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres").withDatabaseName("postgres").withUsername("postgres").withPassword("postgres");

    @DynamicPropertySource
    private static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username=", POSTGRES::getUsername);
        registry.add("spring.datasource.password=", POSTGRES::getPassword);
    }

    private static MockMvc mockMvc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    public void clean() {
        SecurityContextHolder.clearContext();
        userRepository.deleteAll();
    }

    private static void addUser() throws Exception {
        RegisterRequest request = new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL);

        MockHttpServletRequestBuilder requestPost = post(REGISTER_ENDPOINT).content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPost);
    }

    @Test
    void createAuthTokenTest() throws Exception {
        addUser();

        AuthRequest request = new AuthRequest(LOGIN, PASSWORD);

        MockHttpServletRequestBuilder requestPost = post(AUTH_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        String authToken = mockMvc.perform(requestPost)
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        Assertions.assertNotNull(authToken);
    }

    @Test
    void createAuthTokenTestBadRequest() throws Exception {
        addUser();

        AuthRequest request = new AuthRequest(LOGIN, "badPass");

        MockHttpServletRequestBuilder requestPost = post(AUTH_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPost)
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAuthTokenTestNotUser() throws Exception {

        AuthRequest request = new AuthRequest(LOGIN, PASSWORD);

        MockHttpServletRequestBuilder requestPost = post(AUTH_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPost)
                .andExpect(status().isBadRequest());
    }
}
