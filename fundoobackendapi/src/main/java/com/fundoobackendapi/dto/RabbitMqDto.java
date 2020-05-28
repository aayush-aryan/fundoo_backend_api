package com.fundoobackendapi.dto;
import org.springframework.stereotype.Component;
import java.io.Serializable;
@Component
public class RabbitMqDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sendToThatMail;
    private String sendFromThisMail;
    private String subject;
    private String body;

    public String getTo() {
        return this.sendToThatMail;
    }

    public String getFrom() {
        return this.sendFromThisMail;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getBody() {
        return this.body;
    }

    public void setTo(String email) {
        this.sendToThatMail = email;
    }

    public void setFrom(String email) {
        this.sendFromThisMail = email;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

