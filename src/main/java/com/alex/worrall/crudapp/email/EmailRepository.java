package com.alex.worrall.crudapp.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findAllByStatus(EmailStatus status);

    List<Email> findAllByStatusAndSubject(EmailStatus status, String subject);
}
