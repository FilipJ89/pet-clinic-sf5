package com.example.petclinicsf5.repositories.security;

import com.example.petclinicsf5.model.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
