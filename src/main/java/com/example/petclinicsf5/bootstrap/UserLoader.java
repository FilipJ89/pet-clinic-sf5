package com.example.petclinicsf5.bootstrap;

import com.example.petclinicsf5.model.security.Authority;
import com.example.petclinicsf5.model.security.Role;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.repositories.security.AuthorityRepository;
import com.example.petclinicsf5.repositories.security.RoleRepository;
import com.example.petclinicsf5.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@RequiredArgsConstructor
@Component
public class UserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private void loadData() {
        // Vet auths
        Authority readVets = authorityRepository.save(Authority.builder().permission("vets.read").build());

        // Visit auths
        Authority createOrUpdateVisit = authorityRepository.save(Authority.builder().permission("visit.create/update").build());
        Authority readVisit = authorityRepository.save(Authority.builder().permission("visit.read").build());

        //Pet auths
        Authority createPet = authorityRepository.save(Authority.builder().permission("pet.create").build());
        Authority updatePet = authorityRepository.save(Authority.builder().permission("pet.update").build());

        // Owner auths
        Authority createOwnerAdminVet = authorityRepository.save(Authority.builder().permission("adminvet.customer.create").build());
        Authority readOwnerAdminVet = authorityRepository.save(Authority.builder().permission("adminvet.customer.read").build());
        Authority updateOwnerAdminVet = authorityRepository.save(Authority.builder().permission("adminvet.customer.update").build());
        Authority deleteOwnerAdminVet = authorityRepository.save(Authority.builder().permission("adminvet.customer.delete").build());

        Authority createOwnerUser = authorityRepository.save(Authority.builder().permission("user.customer.create").build());
        Authority readOwnerUser = authorityRepository.save(Authority.builder().permission("user.customer.read").build());
        Authority updateOwnerUser = authorityRepository.save(Authority.builder().permission("user.customer.update").build());
        Authority deleteOwnerUser = authorityRepository.save(Authority.builder().permission("user.customer.delete").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        adminRole.setAuthorities(new HashSet<>(Arrays.asList(readVets,createOrUpdateVisit,readVisit, createPet, updatePet,
                createOwnerAdminVet, readOwnerAdminVet, updateOwnerAdminVet, deleteOwnerAdminVet)));

        Role vetRole = roleRepository.save(Role.builder().name("VET").build());
        vetRole.setAuthorities(new HashSet<>(Arrays.asList(readVets,createOrUpdateVisit,readVisit, createPet, updatePet,
                createOwnerAdminVet, readOwnerAdminVet, updateOwnerAdminVet, deleteOwnerAdminVet)));

        Role userRole = roleRepository.save(Role.builder().name("USER").build());
        userRole.setAuthorities(new HashSet<>(Arrays.asList(readVets,createOrUpdateVisit,readVisit, createPet, updatePet,
                createOwnerUser, readOwnerUser, updateOwnerUser, deleteOwnerUser)));

        roleRepository.saveAll(Arrays.asList(adminRole,vetRole, userRole));


        // Accounts creation:
        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("filip-jozwiakowski@gmail.com")
                .role(adminRole)
                .build());

        userRepository.save(User.builder()
                .username("SamX")
                .password(passwordEncoder.encode("password"))
                .email("sam-axe@yahoo.com")
                .role(vetRole)
                .build());

        userRepository.save(User.builder()
                .username("JesPort95")
                .password(passwordEncoder.encode("password"))
                .email("Jess95Port@gmail.com")
                .role(vetRole)
                .build());

        userRepository.save(User.builder()
                .username("MichaelW89")
                .password(passwordEncoder.encode("password"))
                .email("michael.weston@gmail.com")
                .role(userRole)
                .build());

        userRepository.save(User.builder()
                .username("FioGle")
                .password(passwordEncoder.encode("password"))
                .email("fiona.gle@gmail.com")
                .role(userRole)
                .build());

        System.out.println("Loaded " + userRepository.count() + " users...");
    }


    @Transactional
    @Override
    public void run(String... args) {
        long count = authorityRepository.count();

        if (count == 0 ){
            loadData();
        }
    }
}