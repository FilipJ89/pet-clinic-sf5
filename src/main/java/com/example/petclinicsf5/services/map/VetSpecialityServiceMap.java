package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.VetSpeciality;
import com.example.petclinicsf5.services.VetSpecialityService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class VetSpecialityServiceMap extends AbstractMapService<VetSpeciality, Long> implements VetSpecialityService {
    @Override
    public Set<VetSpeciality> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(VetSpeciality object) {
        super.delete(object);
    }

    @Override
    public VetSpeciality save(VetSpeciality object) {
        return super.save(object);
    }

    @Override
    public VetSpeciality findById(Long id) {
        return super.findByID(id);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteByID(id);
    }
}
