package com.flavourfit.Emails;

import com.flavourfit.Exceptions.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Method to send an email
     *
     * @param emailDto -- Email details
     * @throws EmailException
     */
    @Override
    public void sendMail(EmailDto emailDto) throws EmailException {
        logger.info("Entered sendEmail() method");
        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDto.getRecipientEmail());
            mailMessage.setText(emailDto.getEmailBody());
            mailMessage.setSubject(emailDto.getEmailSubject());

            javaMailSender.send(mailMessage);
            logger.info("Sent email to {} successfully.", emailDto.getRecipientEmail());
        } catch (Exception e) {
            logger.error("Failed to Send email to {}.", emailDto.getRecipientEmail());
            throw new EmailException("Error while Sending Mail", e);
        }
    }
}
