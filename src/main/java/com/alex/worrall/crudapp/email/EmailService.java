package com.alex.worrall.crudapp.email;

import com.alex.worrall.crudapp.security.config.JwtTokenUtil;
import com.alex.worrall.crudapp.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.alex.worrall.crudapp.security.config.JwtTokenUtil.ACTION_CLAIM;

@Component
public class EmailService {

    public static final String REGISTRATION_EMAIL = "Confirm Email Registration";
    public static final String APPROVAL_CONFIRMATION = "Approval Confirmation";
    public static final String PASSWORD_RESET = "Password Reset Request";

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private EmailRepository emailRepository;

    @Value("${jwt.lifetime.reg-token}")
    private Long regTokenExpiry = 600L;

    @Value("${jwt.lifetime.password-reset-token}")
    private Long passwordResetTokenExpiry = 600L;

    public void createVerificationEmail(String username, String emailAddress) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ACTION_CLAIM, "EMAIL_VERIFICATION");
        String content = tokenUtil.generateToken(claims, username, regTokenExpiry);
        Email email = new Email(emailAddress, REGISTRATION_EMAIL, content);
        emailRepository.save(email);
    }

    public void createApprovalEmail(String username, String emailAddress) {
        Email email = new Email(emailAddress, APPROVAL_CONFIRMATION, "Welcome");
        emailRepository.save(email);
    }

    @Scheduled(cron = "${scheduled.cron.delete-emails}")
    public void scheduledEmailDeletion() {
        deleteExpiredEmails();
    }

    @Scheduled(cron = "${scheduled.cron.send-emails}")
    public void scheduledEmailSending() {
        sendPendingEmails();
    }



    public void sendPendingEmails() {
        int sentCount = 0;
        List<Email> pendingEmails = emailRepository.findAllByStatus(EmailStatus.Pending);
        for (Email email : pendingEmails) {
            //TODO connect to email server and send the emails
            email.setStatus(EmailStatus.Sent);
            sentCount++;
        }
        emailRepository.saveAll(pendingEmails);
        log.info(String.format("Successfully sent %d emails", sentCount));
    }

    public void deleteExpiredEmails() {
        Date oldestAllowedReg = new Date(System.currentTimeMillis() - regTokenExpiry);
        Date oldestAllowedReset = new Date(System.currentTimeMillis() - passwordResetTokenExpiry);
        List<Email> toDelete = new LinkedList<>();
        toDelete.addAll(getDeletableEmails(oldestAllowedReg, REGISTRATION_EMAIL));
        toDelete.addAll(getDeletableEmails(oldestAllowedReset, PASSWORD_RESET));
        emailRepository.deleteAll(toDelete);
    }

    private List<Email> getDeletableEmails(Date oldestAllowed, String subject) {
        List<Email> deletableEmails = new LinkedList<>();
        List<Email> candidates = emailRepository.findAllByStatusAndSubject(EmailStatus.Sent, subject);
        for (Email candidate : candidates) {
            if (candidate.getCreatedOn().before(oldestAllowed)) {
                deletableEmails.add(candidate);
            }
        }
        return deletableEmails;
    }
}
