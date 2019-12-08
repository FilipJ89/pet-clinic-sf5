package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.Vet;
import com.example.petclinicsf5.services.VetService;
import com.example.petclinicsf5.services.VetSpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class VetServiceMap extends AbstractMapService<Vet, Long> implements VetService {

    private final VetSpecialityService vetSpecialityService;

    @Autowired
    public VetServiceMap(VetSpecialityService vetSpecialityService) {
        this.vetSpecialityService = vetSpecialityService;
    }

    @Override
    public Set<Vet> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(Vet vet) {
        super.delete(vet);
    }

    @Override
    public Vet findById(Long id) {
        return super.findByID(id);
    }

    @Override
    public Vet save(Vet vet) {
        if (vet.getSpecialities().size() > 0) {
            vet.getSpecialities().forEach(speciality -> {
                if (speciality.getId() == null) {
                    vetSpecialityService.save(speciality);
                    speciality.setId(speciality.getId());
                }
            });
        }
        return super.save(vet);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteByID(id);
    }
}
