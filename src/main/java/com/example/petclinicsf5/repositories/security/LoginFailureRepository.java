package com.example.petclinicsf5.repositories.security;

import com.example.petclinicsf5.model.security.LoginFailure;
import com.example.petclinicsf5.model.security.User;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface LoginFailureRepository extends CrudRepository<LoginFailure, Long> {

    List<LoginFailure> findAllByUserAndAndCreatedDateAfter(User user, Timestamp timestamp);
}
