package com.alex.worrall.crudapp.user;

import com.alex.worrall.crudapp.framework.Exceptions.UsernameInUseException;
import com.alex.worrall.crudapp.security.model.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with the username " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.isEnabled(), true, true, true, user.getAuthorities());
    }

    public void createEnabledUser(String email, String password, AuthProvider authProvider,
                                  Role... roles) throws UsernameInUseException {
        if (checkUsernameAvailable(email)) {
            User user = new User(email, email, passwordEncoder.encode(password), roles);
            user.setAuthProvider(authProvider);
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    public boolean checkUsernameAvailable(String username) throws UsernameInUseException {
        if (userRepository.findByUsername(username) != null) {
            throw new UsernameInUseException(String.format("Username %s is already in use", username));
        };
        return true;
    }
}
