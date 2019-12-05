package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.BaseEntity;

import java.util.*;

public abstract class AbstractMapService<T extends BaseEntity,ID extends Long> {

    Map<Long, T> map = new HashMap<>();

    Set<T> findAll(){
        return new HashSet<>(map.values());
    }

    T findByID(ID id) {
        return map.get(id);
    }

    T save(T object){
        if(object != null) {
            if (object.getId() == null) {
                object.setId(getNextID());
            }
            map.put(object.getId(), object);
        }
        return object;
    }

    void deleteByID(ID id) {
        map.remove(id);
    }

    void delete(T object) {
        map.entrySet().removeIf(entry -> entry.equals(object));
    }

    private Long getNextID() {
        Long nextId = null;
        try {
            nextId = Collections.max(map.keySet()) + 1;
        } catch (NoSuchElementException e) {
            nextId = 1L;
        }
        return nextId;
    }


}
