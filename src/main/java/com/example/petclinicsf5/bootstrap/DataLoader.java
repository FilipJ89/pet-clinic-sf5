package com.example.petclinicsf5.bootstrap;

import com.example.petclinicsf5.model.*;
import com.example.petclinicsf5.services.OwnerService;
import com.example.petclinicsf5.services.PetTypeService;
import com.example.petclinicsf5.services.VetService;
import com.example.petclinicsf5.services.VetSpecialityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final VetSpecialityService vetSpecialityService;

    @Autowired
    public DataLoader(OwnerService ownerService, VetService vetService, PetTypeService petTypeService, VetSpecialityService vetSpecialityService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.vetSpecialityService = vetSpecialityService;
    }

    // run Bootstrap only if no data
    @Override
    public void run(String... args) throws Exception {
        int count = petTypeService.findAll().size();
        if (count == 0) {
            loadData();
        }
    }

    private void loadData() {
        // -------------- Pet Types ------------- //
        PetType dog = new PetType();
        dog.setName("dog");
        petTypeService.save(dog);

        PetType cat = new PetType();
        cat.setName("cat");
        petTypeService.save(cat);

        // -------- Vet Speciality Types -------- //
        VetSpeciality radiology = new VetSpeciality();
        radiology.setSpeciality("radiology");
        vetSpecialityService.save(radiology);

        VetSpeciality surgery = new VetSpeciality();
        radiology.setSpeciality("surgery");
        vetSpecialityService.save(surgery);

        VetSpeciality dentistry = new VetSpeciality();
        radiology.setSpeciality("dentistry");
        vetSpecialityService.save(dentistry);

        // --------------- Owner Data ------------ //
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

        // --------------- Vet Data ------------ //

        Vet vet1 = new Vet();
        vet1.setFirstName("Sam");
        vet1.setLastName("Axe");
        vet1.getSpecialities().add(radiology);
        vet1.getSpecialities().add(dentistry);

        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("Jessie");
        vet2.setLastName("Porter");
        vet2.getSpecialities().add(surgery);

        vetService.save(vet2);

        System.out.println("Loaded Vets....");
    }
}
