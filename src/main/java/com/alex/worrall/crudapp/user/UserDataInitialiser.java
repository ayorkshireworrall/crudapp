package com.alex.worrall.crudapp.user;

import com.alex.worrall.crudapp.framework.DataInitialiserModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Transactional
@Component
public class UserDataInitialiser implements DataInitialiserModule {

    @Autowired
    private UserService userService;

    @Override
    public void initialiseData() {
        userService.createUser("User", "Password", Role.USER);
        userService.createUser("Admin", "Password", Role.USER, Role.ADMIN);
    }
}
