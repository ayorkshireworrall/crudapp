package com.alex.worrall.crudapp.user;

import com.alex.worrall.crudapp.security.model.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi {

    @Autowired
    UserService userService;

    @PostMapping("/registration/app")
    public void registerUser(@RequestBody UserRegistration userRegistration) {
        userService.registerUser(userRegistration, AuthProvider.app);
    }

    @PostMapping("/resend/verification/email")
    public void resendVerificationEmail(@RequestParam String username, @RequestParam String email) {
        userService.sendNewVerificationEmail(username, email);
    }

    @PostMapping("/verify/email")
    public void verifyEmail(@RequestParam String email, @RequestParam String token) {
        userService.verifyUserRegistration(email, token);
    }
}
