package com.example.petclinicsf5.bootstrap;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.model.Pet;
import com.example.petclinicsf5.model.PetType;
import com.example.petclinicsf5.model.Vet;
import com.example.petclinicsf5.services.OwnerService;
import com.example.petclinicsf5.services.PetTypeServicw;
import com.example.petclinicsf5.services.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeServicw petTypeServicw;

    @Autowired
    public DataLoader(OwnerService ownerService, VetService vetService, PetTypeServicw petTypeServicw) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeServicw = petTypeServicw;
    }

    @Override
    public void run(String... args) throws Exception {

        PetType dog = new PetType();
        dog.setName("dog");
        petTypeServicw.save(dog);

        PetType cat = new PetType();
        cat.setName("cat");
        petTypeServicw.save(cat);


        Owner owner1 = new Owner();
        owner1.setFirstName("Michael");
        owner1.setLastName("Weston");
        owner1.setAddress("12 Street");
        owner1.setCity("Miami");
        owner1.setTelephone("123456789");

        Pet owner1Pet1 = new Pet();
        owner1Pet1.setName("Max");
        owner1Pet1.setPetType(dog);
        owner1Pet1.setOwner(owner1);
        owner1Pet1.setBirthDate(LocalDate.of(2019,02, 23));

        ownerService.save(owner1);

        Owner owner2 = new Owner();
        owner2.setFirstName("Fiona");
        owner2.setLastName("Glenanne");
        owner2.setAddress("12 Street");
        owner2.setCity("Miami");
        owner2.setTelephone("123456789");

        Pet owner2Pet1 = new Pet();
        owner2Pet1.setName("Cuddles");
        owner2Pet1.setPetType(cat);
        owner2Pet1.setOwner(owner2);
        owner2Pet1.setBirthDate(LocalDate.now());

        ownerService.save(owner2);

        System.out.println("Loaded Owners....");

        Vet vet1 = new Vet();
        vet1.setFirstName("Sam");
        vet1.setLastName("Axe");

        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("Jessie");
        vet2.setLastName("Porter");

        vetService.save(vet2);

        System.out.println("Loaded Vets....");
    }
}
