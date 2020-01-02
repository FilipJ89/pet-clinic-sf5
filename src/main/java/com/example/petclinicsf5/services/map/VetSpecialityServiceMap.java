package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.Speciality;
import com.example.petclinicsf5.services.VetSpecialityService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class VetSpecialityServiceMap extends AbstractMapService<Speciality, Long> implements VetSpecialityService {
    @Override
    public Set<Speciality> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(Speciality object) {
        super.delete(object);
    }

    @Override
    public Speciality save(Speciality object) {
        return super.save(object);
    }

    @Override
    public Speciality findById(Long id) {
        return super.findByID(id);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteByID(id);
    }
}
