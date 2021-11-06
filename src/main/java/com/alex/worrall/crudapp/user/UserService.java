package com.alex.worrall.crudapp.user;

import com.alex.worrall.crudapp.email.EmailService;
import com.alex.worrall.crudapp.framework.Exceptions.EmailInUseException;
import com.alex.worrall.crudapp.framework.Exceptions.InUseException;
import com.alex.worrall.crudapp.framework.Exceptions.InvalidTokenException;
import com.alex.worrall.crudapp.framework.Exceptions.UserNotFoundException;
import com.alex.worrall.crudapp.framework.Exceptions.UsernameInUseException;
import com.alex.worrall.crudapp.security.config.AppOidcUser;
import com.alex.worrall.crudapp.security.config.JwtTokenUtil;
import com.alex.worrall.crudapp.security.model.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.alex.worrall.crudapp.security.config.JwtTokenUtil.ACTION_EMAIL_VERIFICATION;
import static com.alex.worrall.crudapp.security.model.AuthProvider.app;

@Component
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    private JwtTokenUtil tokenUtil;

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
                                  Role... roles) throws InUseException {
        checkUsernameAndEmail(email, email);
        User user = new User(email, email, passwordEncoder.encode(password), roles);
        user.setAuthProvider(authProvider);
        user.setRegisteredOn(new Date(System.currentTimeMillis()));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void postSocialAuthentication(OAuth2User user) {
        if (user instanceof OidcUser) {
            AppOidcUser oidcUser = (AppOidcUser) user;
            User existingUser = userRepository.findByEmail(oidcUser.getEmail());
            if (existingUser == null) {
                register(new UserRegistration(oidcUser.getEmail()), oidcUser.getProvider());
            } else {
                // TODO log user in
            }
        }
    }

    public void register(UserRegistration userRegistration, AuthProvider authProvider) {
        if (app.equals(authProvider)) {
            registerAppUser(userRegistration);
        } else {
            registerSocialUser(userRegistration, authProvider);
        }
    }

    protected void registerAppUser(UserRegistration userRegistration) throws InUseException {
        String email = userRegistration.getEmail();
        String username = userRegistration.getUsername() == null ? email : userRegistration.getUsername();
        checkUsernameAndEmail(username, email);
        Role[] roles = {Role.USER};
        User user = new User(username, email, passwordEncoder.encode(userRegistration.getPassword()), roles);
        user.setAuthProvider(app);
        user.setRegisteredOn(new Date(System.currentTimeMillis()));
        user.setEnabled(false);
        userRepository.save(user);
        emailService.createVerificationEmail(username, email);
    }

    protected void registerSocialUser(UserRegistration userRegistration, AuthProvider authProvider) {
        String email = userRegistration.getEmail();
        String username = userRegistration.getUsername() == null ? email : userRegistration.getUsername();
        checkUsernameAndEmail(username, email);
        Role[] roles = {Role.USER};
        User user = new User(username, email, null, roles);
        user.setAuthProvider(AuthProvider.google);
        user.setRegisteredOn(new Date(System.currentTimeMillis()));
        user.setEnabled(true);
        userRepository.save(user);
    }

    protected void registerGoogleUser(UserRegistration userRegistration) throws InUseException {
        String email = userRegistration.getEmail();
        String username = userRegistration.getUsername() == null ? email : userRegistration.getUsername();
        checkUsernameAndEmail(username, email);
        Role[] roles = {Role.USER};
        User user = new User(username, email, null, roles);
        user.setAuthProvider(AuthProvider.google);
        user.setRegisteredOn(new Date(System.currentTimeMillis()));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void verifyUserRegistration(String email, String token) {
        User user = userRepository.findByEmail(email);
        tokenUtil.assertActionClaimEquals(ACTION_EMAIL_VERIFICATION, token);
        if (user == null) {
            throw new UserNotFoundException(String.format("No registered users found with the " +
                    "email %s", email));
        }
        if (!tokenUtil.validateToken(token, user)) {
            throw new InvalidTokenException(String.format("Token is invalid, either provided " +
                    "user email %s does not match any registered users or token is expired. " +
                    "Please request a new token.", email));
        }
        user.setEnabled(true);
        emailService.createApprovalEmail(user.getUsername(), user.getEmail());
        userRepository.save(user);
    }

    public void sendNewVerificationEmail(String username, String email) {
        emailService.createVerificationEmail(username, email);
    }

    public void registerUserByApp() {

    }

    public void enableUser(String username) {

    }

    public void checkUsernameAndEmail(String username, String email) throws InUseException {
        if (userRepository.findByUsername(username) != null) {
            throw new UsernameInUseException(String.format("Username %s is already in use", username));
        }
        if (userRepository.findByEmail(email) != null) {
            throw new EmailInUseException(String.format("Email %s is already in use", email));
        }
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
