package com.example.petclinicsf5.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Person {

    private String firstName;
    private String lastName;
}
