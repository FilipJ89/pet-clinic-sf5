package com.example.petclinicsf5.repositories.security;

import com.example.petclinicsf5.model.security.User;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByAccountNonLockedAndLastModifiedDateIsBefore(Boolean isNotLocked, Timestamp date);
}
