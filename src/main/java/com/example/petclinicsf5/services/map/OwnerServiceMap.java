package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.services.OwnerService;
import com.example.petclinicsf5.services.PetService;
import com.example.petclinicsf5.services.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {

    private final PetService petService;
    private final PetTypeService petTypeService;

    @Autowired
    public OwnerServiceMap(PetService petService, PetTypeService petTypeService) {
        this.petService = petService;
        this.petTypeService = petTypeService;
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
                            pet.setPetType(petTypeService.save(pet.getPetType()));
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
