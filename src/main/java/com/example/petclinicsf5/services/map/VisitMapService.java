package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.Visit;
import com.example.petclinicsf5.services.VisitService;

import java.util.Set;

public class VisitMapService extends AbstractMapService<Visit, Long> implements VisitService {
    @Override
    public Set<Visit> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(Visit object) {
        super.delete(object);
    }

    @Override
    public Visit save(Visit object) {
        return super.save(object);
    }

    @Override
    public Visit findById(Long aLong) {
        return super.findByID(aLong);
    }

    @Override
    public void deleteById(Long aLong) {
        super.deleteByID(aLong);
    }
}
