package com.example.petclinicsf5.services.springdatajpa;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.repositories.OwnerRepository;
import com.example.petclinicsf5.repositories.PetRepository;
import com.example.petclinicsf5.repositories.PetTypeRepository;
import com.example.petclinicsf5.services.OwnerService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jt on 8/5/18.
 */
@Service
@Profile("springdatajpa")
public class OwnerSDJpaService implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;

    public OwnerSDJpaService(OwnerRepository ownerRepository, PetRepository petRepository,
                             PetTypeRepository petTypeRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public Owner findByLastName(String lastName) {
        return ownerRepository.findByLastName(lastName);
    }

    @Override
    public List<Owner> findAllByEmail(String email) {
        return ownerRepository.findAllByEmail(email);
    }

    @Override
    public List<Owner> findAllByLastNameLikeIgnoreCase(String lastName) {
        return ownerRepository.findAllByLastNameLikeIgnoreCase(lastName);
    }

    @Override
    public List<Owner> findAllByLastNameLikeIgnoreCaseAndEmail(String lastName, String email) {
        return ownerRepository.findAllByLastNameLikeIgnoreCaseAndEmail(lastName,email);
    }

    @Override
    public Set<Owner> findAll() {
        Set<Owner> owners = new HashSet<>();
        ownerRepository.findAll().forEach(owners::add);
        return owners;
    }

    @Override
    public Owner findById(Long aLong) {
        return ownerRepository.findById(aLong).orElse(null);
    }

    @Override
    public Owner save(Owner object) {
        return ownerRepository.save(object);
    }

    @Override
    public void delete(Owner object) {
        ownerRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        ownerRepository.deleteById(aLong);
    }
}