package com.example.petclinicsf5.services.springdatajpa;

import com.example.petclinicsf5.model.Speciality;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("springdatajpa")
public class SpecialityService {

    private final SpecialityService specialityService;

    public SpecialityService(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }


    public Set<Speciality> findAll() {
        Set<Speciality> specialities = new HashSet<>();
        specialityService.findAll().forEach(specialities::add);
        return specialities;
    }

    public void delete(Speciality object) {
        specialityService.delete(object);
    }

    public Speciality save(Speciality object) {
        return specialityService.save(object);
    }

    public Speciality findById(Long id) { return specialityService.findById(id); }

    public void deleteById(Long id) {
        specialityService.deleteById(id);
    }
}
