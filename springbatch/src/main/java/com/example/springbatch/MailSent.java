package com.example.springbatch;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MailSent {

    private String userId;


    @Id
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getMailSent() {
        return mailSent;
    }

    public void setMailSent(Boolean mailSent) {
        this.mailSent = mailSent;
    }

    Boolean mailSent;
}
