package com.example.petclinicsf5.services;

import com.example.petclinicsf5.model.Vet;

import java.util.Set;

public interface VetService {

    Vet findById(Long id);
    Vet save(Vet vet);
    Set<Vet> findAll();
}
