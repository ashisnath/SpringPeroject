package com.example.springbatch;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class User {

    private String userId;


    @Id
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String marks;
    private String name;

    public Boolean getMailInd() {
        return mailInd;
    }

    public void setMailInd(Boolean mailInd) {
        this.mailInd = mailInd;
    }

    private Boolean mailInd;

}
