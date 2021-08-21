package com.example.petclinicsf5.services;

import com.example.petclinicsf5.model.security.Role;
import com.example.petclinicsf5.model.security.User;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ValidationFunctions {

    private OwnerService ownerService;

    public ValidationFunctions(OwnerService ownerService) {this.ownerService = ownerService;}

    public boolean hasUserThisRole(User user, String roleName) {
        return user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList())
                .contains(roleName);
    }

    // checks if email match between user and owner
    public boolean isUserOwnerIdMatched(Long ownerId, User user) {
        return ownerService.findById(ownerId).getEmail().equals(user.getEmail());
    }
}
