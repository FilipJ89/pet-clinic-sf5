package com.example.petclinicsf5.services;

import com.example.petclinicsf5.model.Owner;

public interface OwnerService extends CrudService<Owner, Long> {

    Owner findByLastName(String lastName);

}
