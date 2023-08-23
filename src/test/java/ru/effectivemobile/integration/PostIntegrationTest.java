package ru.effectivemobile.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.effectivemobile.dto.account.RegisterRequest;
import ru.effectivemobile.dto.auth.AuthRequest;
import ru.effectivemobile.dto.post.PostDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostIntegrationTest {
    private static final File IMAGE = new File("./src/test/resources/testFile.jpg");
    private static final String FILE_NAME = "testFile.jpg";
    private static final String IMAGES = "images";
    private static final String PREFIX_FILE_URL = "http://localhost:8090/image/";

    private static final String REGISTER_ENDPOINT = "/v1/account/register";
    private static final String AUTH_ENDPOINT = "/v1/auth/login";
    private static final String POST_ENDPOINT = "/v1/post";
    private static final String PARAM_PAGE_NUMBER = "pageNumber";
    private static final String PARAM_PAGE_SIZE = "pageSize";
    private static final String PARAM_NAME_TITLE = "title";
    private static final String PARAM_NAME_TEXT = "text";
    private static final String TEST_TEXT = "Тестовый текст";
    private static final String TEST_TITLE = "Тестовый заголовок";
    private static final String HEADER_NAME = "Authorization";
    private static final String BEARER = "Bearer ";
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

    private static MockMultipartFile getFile() throws IOException {
        return new MockMultipartFile(
                IMAGES,
                FILE_NAME,
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(IMAGE));
    }

    private static final MockMultipartFile NOT_IMAGE_FILE_WITH_CONTENT = new MockMultipartFile(
            IMAGES,
            "testFile.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Test file content".getBytes()
    );


    private static MockMvc mockMvc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static String token;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        createAuthTokenTest();
    }

    @AfterEach
    public void clean() {
        SecurityContextHolder.clearContext();
    }

    private static void createAuthTokenTest() throws Exception {
        RegisterRequest request = new RegisterRequest(LOGIN, PASSWORD, PASSWORD, EMAIL);

        MockHttpServletRequestBuilder authRequestPost = post(REGISTER_ENDPOINT)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(authRequestPost);

        AuthRequest authRequest = new AuthRequest(LOGIN, PASSWORD);

        MockHttpServletRequestBuilder requestPost = post(AUTH_ENDPOINT)
                .content(objectMapper.writeValueAsString(authRequest))
                .contentType(MediaType.APPLICATION_JSON);

        token = mockMvc.perform(requestPost)
                .andReturn().getResponse()
                .getContentAsString()
                .replace("{\"Authorization\":\"", BEARER);

        token = token.replace("\"}", "");
    }

    @ParameterizedTest
    @MethodSource("argumentsForAddNewPostTest")
    @Disabled
    void addNewPostTest(String title,
                        String text,
                        MockMultipartFile images) throws Exception {

        MockMultipartHttpServletRequestBuilder requestBuilder = multipart(POST_ENDPOINT);
        if (images != null) {
            requestBuilder.file(images);
        }

        MockHttpServletRequestBuilder request =
                requestBuilder
                        .param(PARAM_NAME_TITLE, title)
                        .param(PARAM_NAME_TEXT, text)
                        .header(HEADER_NAME, token);
        String response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        PostDTO postDTO =objectMapper.readValue(response, PostDTO.class);

        Assertions.assertEquals(postDTO.title(),title);
        Assertions.assertEquals(postDTO.text(), text);
        Assertions.assertTrue(postDTO.id() > 0);
        Assertions.assertEquals(postDTO.authorName(), LOGIN);
        Assertions.assertNotNull(postDTO.data());

        if (images == null) {
            Assertions.assertNull(postDTO.imageUrlList());
        } else {
            Assertions.assertNotNull(postDTO.imageUrlList());
            assertTrue(postDTO.imageUrlList().spliterator().tryAdvance(imageUrls ->
                    imageUrls.startsWith(PREFIX_FILE_URL)));
        }


    }

    private static Stream<Arguments> argumentsForAddNewPostTest() throws IOException {
        return Stream.of(
                Arguments.of(
                        TEST_TITLE,
                        TEST_TEXT,
                        getFile()),
                Arguments.of(
                        TEST_TITLE,
                        null,
                        getFile()),
                Arguments.of(
                        TEST_TITLE,
                        null,
                        null)
        );
    }


}
