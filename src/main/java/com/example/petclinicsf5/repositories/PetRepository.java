package com.example.petclinicsf5.repositories;

import com.example.petclinicsf5.model.Pet;
import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends CrudRepository<Pet, Long> {
}
