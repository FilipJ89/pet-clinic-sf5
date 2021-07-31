package com.example.petclinicsf5.repositories.security;

import com.example.petclinicsf5.model.security.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
