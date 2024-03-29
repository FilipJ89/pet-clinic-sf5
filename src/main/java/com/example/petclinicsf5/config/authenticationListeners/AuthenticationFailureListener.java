package com.example.petclinicsf5.config.authenticationListeners;

import com.example.petclinicsf5.model.security.LoginFailure;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.repositories.security.LoginFailureRepository;
import com.example.petclinicsf5.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        log.debug("Login failure");

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            String logAttemptUsername = (String) token.getPrincipal();

            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();

            if (token.getPrincipal() instanceof String) {
                log.debug("Attempted username: " + logAttemptUsername);
                builder.username(logAttemptUsername);
                userRepository.findByUsername(logAttemptUsername).ifPresent(builder::user);
            }

            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                log.debug("Source IP: " + details.getRemoteAddress());
                builder.sourceIp(details.getRemoteAddress());
            }
            LoginFailure failure = loginFailureRepository.save(builder.build());
            log.debug("Login failure saved. ID: " + failure.getId());

            if (failure.getUser() != null) {
                lockUserAccount(failure.getUser());
            }
        }
    }

    private void lockUserAccount(User user) {
        Timestamp checkPeriod = Timestamp.valueOf(LocalDateTime.now().minusHours(1));
        List<LoginFailure> failures = loginFailureRepository.findAllByUserAndAndCreatedDateAfter(user, checkPeriod);

        if (failures.size() > 2) {
            log.debug("Locking account for user '" + user.getUsername() + "'");
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }
}
