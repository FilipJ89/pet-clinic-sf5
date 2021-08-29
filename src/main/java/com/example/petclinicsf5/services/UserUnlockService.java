package com.example.petclinicsf5.services;

import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserUnlockService {

    private final UserRepository userRepository;

    @Scheduled(fixedRate = 60000)
    public void unlockUsers() {
        log.debug("Checking if any accounts to unlock...");
        Timestamp lockDate = Timestamp.valueOf(LocalDateTime.now().minusMinutes(30));
        List<User> lockedUsers = userRepository.findAllByAccountNonLockedAndLastModifiedDateIsBefore(false,lockDate);

        if (lockedUsers.size()>0) {
            log.debug("Unlocking account(s)...");
            lockedUsers.forEach(user -> {
                user.setAccountNonLocked(true);
                userRepository.save(user);
                log.debug("Unlocked account for user '" + user.getUsername() + "'");
            });
        }
    }
}
