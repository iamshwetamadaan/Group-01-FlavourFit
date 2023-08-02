package com.flavourfit.Emails;

import com.flavourfit.Exceptions.EmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.junit.jupiter.api.Assertions.*;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMail() throws Exception {
        // Define the EmailDto
        EmailDto emailDto = new EmailDto("recipient@example.com", "Email Body", "Email Subject");

        // Happy Path: Email sent successfully
        doNothing().when(javaMailSender).send(new SimpleMailMessage());
        emailService.sendMail(emailDto); // No exception should be thrown

        // Sad Path: Exception while sending email
        doThrow(new RuntimeException("Error sending email")).when(javaMailSender).send(new SimpleMailMessage());

        try {
            emailService.sendMail(emailDto);
        } catch (EmailException e) {
            // Assert the exception message
            assertEquals("Error while Sending Mail", e.getMessage());
        }
    }
}
