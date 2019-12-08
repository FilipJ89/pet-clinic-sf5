package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.services.OwnerService;
import com.example.petclinicsf5.services.PetService;
import com.example.petclinicsf5.services.PetTypeServicw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {

    private final PetService petService;
    private final PetTypeServicw petTypeServicw;

    @Autowired
    public OwnerServiceMap(PetService petService, PetTypeServicw petTypeServicw) {
        this.petService = petService;
        this.petTypeServicw = petTypeServicw;
    }

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

        if (object != null) {
            if(object.getPets() != null) {
                object.getPets().forEach( pet -> {
                    if (pet.getPetType() != null) {
                        if (pet.getPetType().getId() == null) {
                            pet.setPetType(petTypeServicw.save(pet.getPetType()));
                        }
                    } else {
                        throw new RuntimeException("Pet Type is Required");
                    }

                    if (pet.getId() == null) {
                        petService.save(pet);
                        pet.setId(pet.getId());
                    }
                });
            }
        }
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
