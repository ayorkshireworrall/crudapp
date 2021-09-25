package com.alex.worrall.crudapp.user;

import com.alex.worrall.crudapp.framework.DataInitialiserModule;
import com.alex.worrall.crudapp.security.model.AuthProvider;
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
        userService.createEnabledUser("User", "Password", AuthProvider.app, Role.USER);
        userService.createEnabledUser("Admin", "Password",AuthProvider.app, Role.USER, Role.ADMIN);
    }
}
