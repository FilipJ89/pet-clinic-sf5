package com.example.petclinicsf5.services;

import com.example.petclinicsf5.model.Owner;

/**
 * Created by jt on 7/18/18.
 */
public interface OwnerService extends CrudService<Owner, Long> {

    Owner findByLastName(String lastName);

}
