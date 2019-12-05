package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.services.OwnerService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {


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
        return super.save(object);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteByID(id);
    }

    @Override
    public Owner findByLastName(String lastName) {
        return null;
    }
}
