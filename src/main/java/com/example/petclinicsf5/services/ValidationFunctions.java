package com.example.petclinicsf5.services;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.model.security.Role;
import com.example.petclinicsf5.model.security.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ValidationFunctions {

    private OwnerService ownerService;
    private PetService petService;

    public ValidationFunctions(OwnerService ownerService, PetService petService) {
        this.ownerService = ownerService;
        this.petService = petService;
    }

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

    public Owner getOwnerByPetId(Long petId) {
        return petService.findById(petId).getOwner();
    }
}
