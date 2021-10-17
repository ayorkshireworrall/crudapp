package com.alex.worrall.crudapp.email;

import liquibase.pro.packaged.C;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "email")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String addressee;

    @Column
    private String subject;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    @Column
    private final Date createdOn;

    public Email() {
        this.createdOn = new Date(System.currentTimeMillis());
    }

    public Email(String addressee, String subject, String content) {
        this.addressee = addressee;
        this.subject = subject;
        this.content = content;
        this.status = EmailStatus.Pending;
        this.createdOn = new Date(System.currentTimeMillis());
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
}
