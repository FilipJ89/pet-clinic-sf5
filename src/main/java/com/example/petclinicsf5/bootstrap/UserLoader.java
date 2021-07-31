package com.example.petclinicsf5.bootstrap;

import com.example.petclinicsf5.model.security.Authority;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.repositories.security.AuthorityRepository;
import com.example.petclinicsf5.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void loadData() {
        Authority admin = authorityRepository.save(Authority.builder().role("ADMIN").build());
        Authority owner = authorityRepository.save(Authority.builder().role("OWNER").build());
        Authority vet = authorityRepository.save(Authority.builder().role("VET").build());

        userRepository.save(User.builder()
                .username("MichaelW89")
                .password(passwordEncoder.encode("password"))
                .email("michael.w@gmail.com")
                .authority(owner)
                .build());

        userRepository.save(User.builder()
                .username("FioGle")
                .password(passwordEncoder.encode("password"))
                .email("fiogle@gmail.com")
                .authority(owner)
                .build());

        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("filip-jozwiakowski@gmail.com")
                .authority(admin)
                .build());

        System.out.println(userRepository.count() + " users loaded...");
    }


    @Override
    public void run(String... args) throws Exception {
        long count = authorityRepository.count();

        if (count == 0 ){
            loadData();
        }
    }
}