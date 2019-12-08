package com.example.petclinicsf5.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Vet extends Person {
    private Set<VetSpeciality> specialities;
}
