package ru.effectivemobile.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import ru.effectivemobile.dto.account.ChangeEmailRequest;
import ru.effectivemobile.dto.account.ChangePasswordRequest;
import ru.effectivemobile.dto.account.RegisterRequest;
import ru.effectivemobile.dto.account.RegisterResponse;
import ru.effectivemobile.repository.UserRepository;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountIntegrationTest {
    private static final String REGISTER_ENDPOINT = "/v1/account/register";
    private static final String PASSWORD_ENDPOINT = "/v1/account/password";
    private static final String AUTH_ENDPOINT = "/v1/auth/login";
    private static final String EMAIL_ENDPOINT = "/v1/account/email";
    private static final String LOGIN = "testLogin";
    private static final String PASSWORD = "testPassword";
    private static final String EMAIL = "test@mail.ru";
    private static final String BEARER = "Bearer ";
    private static final String HEADER_AUTH = "Authorization";
    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");

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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @AfterEach
    public void clean() {
        SecurityContextHolder.clearContext();
        userRepository.deleteAll();
    }


    @Test
    void addNewUserTestValidData() throws Exception {
        RegisterRequest request = new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL);

        MockHttpServletRequestBuilder requestPost = post(REGISTER_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        String response = mockMvc.perform(requestPost)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RegisterResponse registerResponse = objectMapper.readValue(response, RegisterResponse.class);

        assertNotNull(registerResponse.id());
        assertEquals(registerResponse.login(), LOGIN);
        assertEquals(registerResponse.email(), EMAIL);
    }

    @ParameterizedTest
    @MethodSource("argumentsForAddNewUserTestBadRequest")
    void addNewUserTestBadRequest(
            String login,
            String password,
            String passwordConfirm,
            String email
    ) throws Exception {
        RegisterRequest request = new RegisterRequest(login, password, passwordConfirm, email);

        MockHttpServletRequestBuilder requestPost = post(REGISTER_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPost)
                .andExpect(status().isBadRequest());
    }


    private static Stream<Arguments> argumentsForAddNewUserTestBadRequest() {
        return Stream.of(
                Arguments.of(
                        LOGIN,
                        PASSWORD,
                        " ",
                        EMAIL),
                Arguments.of(
                        null,
                        PASSWORD,
                        PASSWORD,
                        EMAIL),
                Arguments.of(
                        LOGIN,
                        null,
                        PASSWORD,
                        EMAIL),
                Arguments.of(
                        LOGIN,
                        PASSWORD,
                        PASSWORD,
                        null),
                Arguments.of(
                        " ",
                        PASSWORD,
                        PASSWORD,
                        null)
        );
    }

    @Test
    void addNewUserTestLoginExists() throws Exception {
        RegisterRequest request = new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL);

        MockHttpServletRequestBuilder requestPost = post(REGISTER_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPost)
                .andExpect(status().isCreated());

        mockMvc.perform(requestPost)
                .andExpect(status().isBadRequest());

    }

    @Test
    void addNewUserTestEmailExists() throws Exception {
        addUser();

        RegisterRequest request = new RegisterRequest("Test", PASSWORD, PASSWORD, EMAIL);
        MockHttpServletRequestBuilder requestPost = put(PASSWORD_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPost)
                .andExpect(status().isBadRequest());
    }

    @Test
    @Disabled
    void setPasswordTestValidData() throws Exception {
        addUser();

        ChangePasswordRequest request = new ChangePasswordRequest("newPass", "newPass");
        MockHttpServletRequestBuilder requestPut = put(PASSWORD_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPut)
                .andExpect(status().isOk());
    }

    private static void addUser() throws Exception {
        RegisterRequest request = new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL);

        MockHttpServletRequestBuilder requestPost = post(REGISTER_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPost);
    }

    @Test
    void checkUnauthorized() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        RegisterRequest request = new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL);

        MockHttpServletRequestBuilder requestPost = post(REGISTER_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPost)
                .andExpect(status().isCreated());

        ChangePasswordRequest passwordRequest = new ChangePasswordRequest("newPass", "newPass");
        MockHttpServletRequestBuilder requestPut = put(PASSWORD_ENDPOINT)
                .content(objectMapper.writeValueAsString(passwordRequest))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestPut)
                .andExpect(status().isUnauthorized());

        ChangeEmailRequest emailRequest = new ChangeEmailRequest("newEmail@test.ru");

        MockHttpServletRequestBuilder emailRequestPut = put(EMAIL_ENDPOINT)
                .content(objectMapper.writeValueAsString(emailRequest))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(emailRequestPut)
                .andExpect(status().isUnauthorized());

    }


}
