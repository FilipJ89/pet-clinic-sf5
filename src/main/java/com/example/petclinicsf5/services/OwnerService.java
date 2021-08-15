package com.example.petclinicsf5.services;

import com.example.petclinicsf5.model.Owner;

import java.util.List;

public interface OwnerService extends CrudService<Owner, Long> {

    Owner findByLastName(String lastName);

    List<Owner> findAllByEmail (String email);

    List<Owner> findAllByLastNameLikeIgnoreCase(String lastName);

    List<Owner> findAllByLastNameLikeIgnoreCaseAndEmail(String lastName, String email);
}
