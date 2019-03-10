package com.beehive.riki.log;

import com.pancabudi.technic.users.AppUser;

import javax.persistence.*;
import java.util.Date;

@Entity
public class LogUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String method;
    private String endpoint;
    private String content;
    private Date systemDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "logged_user")
    private AppUser loggedUser;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    public AppUser getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(AppUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    public LogUser() {

    }
}
