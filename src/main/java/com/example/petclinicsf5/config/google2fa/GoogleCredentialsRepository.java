package com.example.petclinicsf5.config.google2fa;

import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.repositories.security.UserRepository;
import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleCredentialsRepository implements ICredentialRepository {

    private final UserRepository userRepository;


    @Override
    public String getSecretKey(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(null);
        return user.getUser2faSecret();
    }

    @Override
    public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
        User user = userRepository.findByUsername(userName).orElseThrow(null);
        user.setUser2faSecret(secretKey);
        user.setIsUser2fa(true);
        userRepository.save(user);
        log.debug("2FA for user '" + userName + "' saved");
    }
}
