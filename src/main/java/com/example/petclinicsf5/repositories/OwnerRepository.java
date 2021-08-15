package com.example.petclinicsf5.repositories;

import com.example.petclinicsf5.model.Owner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OwnerRepository extends CrudRepository<Owner, Long> {

    Owner findByLastName(String lastName);

    List<Owner> findAllByEmail(String email);

    List<Owner> findAllByLastNameLikeIgnoreCase(String lastName);

    List<Owner> findAllByLastNameLikeIgnoreCaseAndEmail(String lastName, String email);
}
