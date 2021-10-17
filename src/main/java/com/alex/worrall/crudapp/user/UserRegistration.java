package com.alex.worrall.crudapp.user;


import javax.validation.constraints.NotNull;

public class UserRegistration {

    @NotNull(message = "Enter your email")
    private String email;

    @NotNull(message = "Enter a password")
    private String password;

    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
