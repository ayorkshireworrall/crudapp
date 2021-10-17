package com.alex.worrall.crudapp.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class AppAuthenticationEntryPoint implements Serializable, AuthenticationEntryPoint {

    @Value("${login.failure.delay}")
    int failedLogonDelayMs;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        delayBeforeSendingAuthenticationFailure();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorised");
        response.getWriter().print("Unable to login");
    }

    private void delayBeforeSendingAuthenticationFailure() {
        try {
            Thread.sleep(failedLogonDelayMs);
        }
        catch (InterruptedException ignored) {
        }
    }

}
