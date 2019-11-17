package com.example.petclinicsf5.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Pet {

    private PetType petType;
    private Owner owner;
    private LocalDate birthDate;
}
