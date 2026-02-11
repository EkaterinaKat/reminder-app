package org.katyshevtseva.functional;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.katyshevtseva.util.Utils.normalizeWhitespace;

public class EmailReminderE2ETest extends BaseReminderE2ETest {

    private static final String GREEN_MAIL_USERNAME = "test@mail.ru";
    private static final String GREEN_MAIL_PASSWORD = "test";

    private static final String USER_MAIL = "user@mail.ru";

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> 3025);
        registry.add("spring.mail.username", () -> GREEN_MAIL_USERNAME);
        registry.add("spring.mail.password", () -> GREEN_MAIL_PASSWORD);
    }

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(
                    GreenMailConfiguration.aConfig().withUser(GREEN_MAIL_USERNAME, GREEN_MAIL_PASSWORD)
            )
            .withPerMethodLifecycle(false);

    @BeforeEach
    void mailSetUp() throws Exception {
        greenMail.purgeEmailFromAllMailboxes();
    }

    @Test
    void shouldSendEmailWithCorrectContentWhenTimeArrives() throws Exception {
        updateProfile(USER_MAIL, null);
        createReminder(TITLE, DESCRIPTION, LocalDateTime.now().plusSeconds(5));

        await()
                .atMost(10, SECONDS)
                .pollInterval(1, SECONDS)
                .untilAsserted(() -> assertThat("Email didn't arrive on time",
                        greenMail.getReceivedMessages().length,
                        equalTo(1)));

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];

        assertThat("Subject of the email must be correct",
                receivedMessage.getSubject(),
                equalTo(TITLE));

        assertThat("Content of the email must be correct",
                normalizeWhitespace(receivedMessage.getContent().toString()),
                equalTo(String.format("%s\n%s", TITLE, DESCRIPTION)));
    }

    @Test
    void shouldNotSendEmailBeforeRemindTime() {
        updateProfile(USER_MAIL, null);
        createReminder(TITLE, DESCRIPTION, LocalDateTime.now().plusSeconds(10));

        await()
                .alias("Email should not be sent ahead of time")
                .atMost(10, SECONDS)
                .during(9, SECONDS)
                .until(() -> greenMail.getReceivedMessages().length == 0);
    }
}
