package com.example.petclinicsf5.services.springdatajpa;

import com.example.petclinicsf5.model.Vet;
import com.example.petclinicsf5.repositories.VetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("springdatajpa")
public class VetService {

    private final VetRepository vetRepository;

    public VetService(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    public Set<Vet> findAll() {
        Set<Vet> vets = new HashSet<>();
        vetRepository.findAll().forEach(vets::add);
        return vets;
    }

    public void delete(Vet vet) {
        vetRepository.delete(vet);
    }

    public Vet findById(Long id) {
        return vetRepository.findById(id).orElse(null);
    }

    public Vet save(Vet vet) {
       return vetRepository.save(vet);
    }

    public void deleteById(Long id) {
        vetRepository.deleteById(id);
    }
}
