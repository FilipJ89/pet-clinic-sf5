package com.example.petclinicsf5.services.springdatajpa;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.repositories.OwnerRepository;
import com.example.petclinicsf5.repositories.PetRepository;
import com.example.petclinicsf5.repositories.PetTypeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("springdatajpa")
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;

    public OwnerService(OwnerRepository ownerRepository, PetRepository petRepository, PetTypeRepository petTypeRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
    }

    public Owner findByLastName(String lastName) {
        return ownerRepository.findByLastName(lastName);
    }

    public Set<Owner> findAll() {
        Set<Owner> owners = new HashSet<>();
        ownerRepository.findAll().forEach(owners::add);
        return owners;
    }

    public Owner findById(Long id) {
        return ownerRepository.findById(id).orElse(null);
    }

    public Owner save(Owner object) {
        return ownerRepository.save(object);
    }

    public void delete(Owner owner) {
        ownerRepository.delete(owner);
    }

    public void deleteById(Long id) {
        ownerRepository.deleteById(id);
    }
}
