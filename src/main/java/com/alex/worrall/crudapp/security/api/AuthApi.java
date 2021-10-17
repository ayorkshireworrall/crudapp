package com.alex.worrall.crudapp.security.api;

import com.alex.worrall.crudapp.security.config.JwtTokenUtil;
import com.alex.worrall.crudapp.security.model.UsernameAndPassword;
import com.alex.worrall.crudapp.security.model.JwtResponse;
import com.alex.worrall.crudapp.user.User;
import com.alex.worrall.crudapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthApi {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UsernameAndPassword authenticationRequest) throws Exception {

        Authentication auth =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        final UserDetails user = userService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(user);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/session")
    public String createSession(@RequestBody UsernameAndPassword usernameAndPassword,
                              HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        request.getSession().invalidate();
        Authentication auth =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        usernameAndPassword.getUsername(), usernameAndPassword.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }


}
