package com.alex.worrall.crudapp.user;

import com.alex.worrall.crudapp.security.model.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Entity(name = "user")
public class User implements UserDetails{

    public User() {
    }

    public User(String username, String password, Role... roles) {
        this.username = username;
        this.password = password;
        this.roleNames = rolesToString(roles);
    }

    public User(String username, String email, String password, Role... roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleNames = rolesToString(roles);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    @Column
    private String password;

    @Column
    private boolean enabled;

    @Column(name = "roles")
    private String roleNames;

    @Column
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column
    private String providerId;

    @Column
    private Date registeredOn;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles();
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(enabled);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    static String rolesToString(Role[] roles) {
        if (roles == null || roles.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < roles.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(roles[i].name());
        }
        return sb.toString();
    }

    public Collection<String> roleNames() {
        if (roleNames == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(roleNames.split(","));
    }

    public Collection<Role> roles() {
        return roleNames().stream()
                .map(Role::valueOf)
                .collect(Collectors.toList());
    }
}
