package org.katyshevtseva.functional;

import com.katyshevtseva.dto.ReminderRequestDto;
import com.katyshevtseva.dto.UpdateProfileRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseE2ETest {

    @LocalServerPort
    private int port;

    @Value("${security.oauth2.token}")
    private String token;

    protected static final String TITLE = "title";
    protected static final String DESCRIPTION = "description";

    protected static final String HEADER_AUTHORIZATION = "Authorization";
    protected static final String BEARER_PREFIX = "Bearer ";
    protected static final int STATUS_OK = 200;
    protected static final int STATUS_CREATED = 201;

    protected static final String ENDPOINT_PROFILE = "/profile";
    protected static final String ENDPOINT_REMINDER = "/reminder";

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibername.ddl-auto", () -> "validate");
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
//        RestAssured.basePath = "/api"; //todo
    }

    protected void updateProfile(String email, String telegram) {
        UpdateProfileRequestDto dto = new UpdateProfileRequestDto().email(email).telegram(telegram);

        given()
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .put(ENDPOINT_PROFILE)
                .then()
                .statusCode(STATUS_OK);
    }

    protected void deleteProfile() {
        given()
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .when()
                .delete(ENDPOINT_PROFILE)
                .then()
                .statusCode(STATUS_OK);
    }

    protected void createReminder(String title, String description, LocalDateTime remind) {
        ReminderRequestDto dto = new ReminderRequestDto().title(title).description(description).remind(remind);

        given()
                .header(HEADER_AUTHORIZATION, BEARER_PREFIX + token)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(ENDPOINT_REMINDER)
                .then()
                .statusCode(STATUS_CREATED);
    }
}
