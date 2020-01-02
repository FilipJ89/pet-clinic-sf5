package com.example.petclinicsf5.services.springdatajpa;

import com.example.petclinicsf5.model.Visit;
import com.example.petclinicsf5.repositories.VisitRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("springdatajpa")
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Set<Visit> findAll() {
        Set<Visit> visits = new HashSet<>();
        visitRepository.findAll().forEach(visits::add);
        return visits;
    }

    public void delete(Visit visit) {
        visitRepository.delete(visit);
    }

    public Visit findById(Long id) {
        return visitRepository.findById(id).orElse(null);
    }

    public Visit save(Visit visit) {
        return visitRepository.save(visit);
    }

    public void deleteById(Long id) {
        visitRepository.deleteById(id);
    }
}
