package com.example.petclinicsf5.services;

import com.example.petclinicsf5.model.Pet;

import java.util.Set;

public interface PetService {
    
    Pet findById(Long id);
    Pet save(Pet pet);
    Set<Pet> findAll();
}
