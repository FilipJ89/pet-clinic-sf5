package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.services.CrudService;

import java.util.Set;

public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements CrudService<Owner, Long> {


    @Override
    public Set<Owner> findAll() {
        return super.findAll();
    }
    @Override
    public void delete(Owner owner) {
        super.delete(owner);
    }
    @Override
    public Owner findById(Long id) {
        return this.findByID(id);
    }

    @Override
    public Owner save(Owner object) {
        return super.save(object.getId(),object);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteByID(id);
    }
}
