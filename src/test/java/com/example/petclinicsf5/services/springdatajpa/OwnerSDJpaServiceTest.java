package com.example.petclinicsf5.services.springdatajpa;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.repositories.OwnerRepository;
import com.example.petclinicsf5.repositories.PetRepository;
import com.example.petclinicsf5.repositories.PetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {

    @Mock
    OwnerRepository ownerRepository;

    @Mock
    PetRepository petRepository;

    @Mock
    PetTypeRepository petTypeRepository;

    @InjectMocks
    OwnerSDJpaService service;

    Owner testOwner;
    static final Long TEST_ID = 1L;
    static final String TEST_LASTNAME = "Smith";

    @BeforeEach
    void setUp() {
        testOwner = Owner.builder().id(TEST_ID).lastName(TEST_LASTNAME).build();
    }

    @Test
    void findByLastName() {
        when(ownerRepository.findByLastName(any())).thenReturn(testOwner);

        Owner retrievedOwner = service.findByLastName(TEST_LASTNAME);
        assertEquals(TEST_LASTNAME,retrievedOwner.getLastName());
    }

    @Test
    void findAll() {
        Owner testOwner2 = Owner.builder().id(2L).build();

        Set<Owner> ownerSet = new HashSet<>();
        ownerSet.add(testOwner);
        ownerSet.add(testOwner2);

        when(ownerRepository.findAll()).thenReturn(ownerSet);
        Set<Owner> owners = service.findAll();

        assertNotNull(owners);
        assertEquals(2,owners.size());
    }

    @Test
    void findByIdFound() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(testOwner));
        Owner owner = service.findById(TEST_ID);

        assertNotNull(owner);
        assertEquals(TEST_ID, owner.getId());
    }

    @Test
    void findByIdNotFound() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());
        Owner owner = service.findById(TEST_ID);

        assertNull(owner);
    }

    @Test
    void save() {
        when(ownerRepository.save(any())).thenReturn(testOwner);
        Owner owner = service.save(testOwner);

        assertNotNull(owner);
    }

    @Test
    void delete() {
        service.delete(testOwner);
        // times not needed here as 1 is by default
        verify(ownerRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(TEST_ID);
        verify(ownerRepository).deleteById(anyLong());
    }
}