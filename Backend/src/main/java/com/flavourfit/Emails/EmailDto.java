package com.flavourfit.Emails;

public class EmailDto {
    private String recipientEmail;
    private String emailBody;
    private String emailSubject;

    public EmailDto() {
    }

    public EmailDto(String recipientEmail, String emailBody, String emailSubject) {
        this.recipientEmail = recipientEmail;
        this.emailBody = emailBody;
        this.emailSubject = emailSubject;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
}
