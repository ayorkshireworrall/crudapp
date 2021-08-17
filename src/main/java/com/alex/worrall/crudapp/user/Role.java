package com.alex.worrall.crudapp.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    USER(Names.USER),
    ADMIN(Names.ADMIN);

    public class Names {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }


    @Override
    public String toString() {
        return this.name;
    }

    public static Role fromAuthority(String authority) {
        for (Role role : Role.values()) {
            if (role.getAuthority().equals(authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role matching " + authority + " found");
    }
}
