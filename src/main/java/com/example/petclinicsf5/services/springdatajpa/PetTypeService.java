package com.example.petclinicsf5.services.springdatajpa;

import com.example.petclinicsf5.model.PetType;
import com.example.petclinicsf5.repositories.PetTypeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("springdatajpa")
public class PetTypeService {

    private final PetTypeRepository petTypeRepository;

    public PetTypeService(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    public Set<PetType> findAll() {
        Set<PetType> petTypes = new HashSet<>();
        petTypeRepository.findAll().forEach(petTypes::add);
        return petTypes;
    }

    public void delete(PetType petType) {
        petTypeRepository.delete(petType);
    }

    public PetType save(PetType petType) {
        return petTypeRepository.save(petType);
    }

    public PetType findById(Long id) {
        return petTypeRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        petTypeRepository.deleteById(id);
    }

}
