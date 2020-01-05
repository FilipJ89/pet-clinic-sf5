package com.example.petclinicsf5.services.map;

import com.example.petclinicsf5.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OwnerMapServiceTest {

    OwnerMapService ownerMapService;
    Long testId = 1L;
    String testLastNAme = "Smith";

    @BeforeEach
    void setUp() {
        ownerMapService = new OwnerMapService(new PetTypeMapService(), new PetMapService());
        ownerMapService.save(Owner.builder().id(testId).lastName(testLastNAme).address("lala").city("Lodz").build());
    }

    @Test
    void findAll() {
        Set<Owner> ownerSet = ownerMapService.findAll();
        assertEquals(1, ownerSet.size());
    }

    @Test
    void findById() {
        Owner ownerByid = ownerMapService.findById(testId);
        assertEquals(testId,ownerByid.getId());
    }

    @Test
    void saveExistingId() {
        Long existingId = 2L;
        Owner savedOwner = ownerMapService.save(Owner.builder().id(2L).build());

        assertEquals(existingId,savedOwner.getId());
    }

    @Test
    void saveNewId() {
        Owner savedOwner = ownerMapService.save(Owner.builder().build());

        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void delete() {
        ownerMapService.delete(ownerMapService.findById(testId));
        assertEquals(0,ownerMapService.findAll().size());
    }

    @Test
    void deleteById() {
        ownerMapService.deleteById(testId);
        assertEquals(0,ownerMapService.findAll().size());
    }

    @Test
    void findByLastName() {
        Owner owner = ownerMapService.findByLastName(testLastNAme);
        assertEquals(testLastNAme,owner.getLastName());
    }

    @Test
    void findByLastNameNotFound() {
        Owner owner = ownerMapService.findByLastName("nope");
        assertNull(owner);
    }
}