package com.example.petclinicsf5.bootstrap;

import com.example.petclinicsf5.model.*;
import com.example.petclinicsf5.repositories.security.AuthorityRepository;
import com.example.petclinicsf5.repositories.security.UserRepository;
import com.example.petclinicsf5.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Created by jt on 7/25/18.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialityService specialtyService;
    private final VisitService visitService;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    public DataLoader(OwnerService ownerService, VetService vetService, PetTypeService petTypeService,
                      SpecialityService specialtyService, VisitService visitService,
                      AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialtyService = specialtyService;
        this.visitService = visitService;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        int count = petTypeService.findAll().size();

        if (count == 0 ){
            loadData();
        }
    }

    private void loadData() {
        PetType dog = new PetType();
        dog.setName("Dog");
        PetType savedDogPetType = petTypeService.save(dog);

        PetType cat = new PetType();
        cat.setName("Cat");
        PetType savedCatPetType = petTypeService.save(cat);

        Speciality radiology = new Speciality();
        radiology.setDescription("Radiology");
        Speciality savedRadiology = specialtyService.save(radiology);

        Speciality surgery = new Speciality();
        surgery.setDescription("Surgery");
        Speciality savedSurgery = specialtyService.save(surgery);

        Speciality dentistry = new Speciality();
        dentistry.setDescription("Dentistry");
        Speciality savedDentistry = specialtyService.save(dentistry);

        Owner owner1 = new Owner();
        owner1.setFirstName("Michael");
        owner1.setLastName("Weston");
        owner1.setAddress("123 Brickerel");
        owner1.setCity("Miami");
        owner1.setTelephone("1231231234");

        Pet mikesPet = new Pet();
        mikesPet.setPetType(savedDogPetType);
        mikesPet.setOwner(owner1);
        mikesPet.setBirthDate(LocalDate.now());
        mikesPet.setName("Rosco");
        owner1.getPets().add(mikesPet);

        ownerService.save(owner1);

        Owner owner2 = new Owner();
        owner2.setFirstName("Fiona");
        owner2.setLastName("Glenanne");
        owner2.setAddress("12 Disney");
        owner2.setCity("Chicago");
        owner2.setTelephone("987654321");

        Pet fionasCat = new Pet();
        fionasCat.setName("Just Cat");
        fionasCat.setOwner(owner2);
        fionasCat.setBirthDate(LocalDate.now());
        fionasCat.setPetType(savedCatPetType);
        owner2.getPets().add(fionasCat);

        ownerService.save(owner2);

        Owner owner3 = new Owner();
        owner3.setFirstName("Philip");
        owner3.setLastName("Westrock");
        owner3.setAddress("Nanana");
        owner3.setCity("Lala");
        owner3.setTelephone("44345343");

        Pet mikepet = new Pet();
        mikepet.setName("Zorro");
        mikepet.setOwner(owner3);
        mikepet.setBirthDate(LocalDate.now());
        mikepet.setPetType(savedCatPetType);
        owner3.getPets().add(mikepet);

        ownerService.save(owner3);

        Visit catVisit = new Visit();
        catVisit.setPet(fionasCat);
        catVisit.setDate(LocalDate.now());
        catVisit.setDescription("Sneezy Kitty");

        visitService.save(catVisit);

        System.out.println("Loaded " + ownerService.findAll().size() + " owners...");

        Vet vet1 = new Vet();
        vet1.setFirstName("Sam");
        vet1.setLastName("Axe");
        vet1.getSpecialities().add(savedRadiology);

        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("Jessie");
        vet2.setLastName("Porter");
        vet2.getSpecialities().add(savedSurgery);
        vet2.getSpecialities().add(savedDentistry);

        vetService.save(vet2);

        System.out.println("Loaded " + vetService.findAll().size() + " vets...");


//        Authority owner = new Authority();
//        owner.setRole("OWNER");
//
//        User user1 = new User();
//        user1.setUsername("MichaelW89");
//        user1.setPassword(OwnPasswordEncoderFactories.createDelegatingPasswordEncoder()
//                .encode("{noop}owner"));
//        user1.setEmail("michael.w@gmail.com");
//        user1.getAuthorities().add(owner);
//        userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setUsername("FioGle");
//        user2.setPassword(OwnPasswordEncoderFactories.createDelegatingPasswordEncoder()
//                .encode("{bcrypt15}$2y$15$D3VFie0wy4NN/k8xfgRsNuZ3Zx6ya.XQkbUcc.26UwXMd09v/ilue"));
//        user2.setEmail("fiogle@gmail.com");
//        user2.getAuthorities().add(owner);
//        userRepository.save(user2);
//
//        owner.getUsers().add(user1);
//        owner.getUsers().add(user2);
//        authorityRepository.save(owner);
//
//        System.out.println("Loaded Users and Authorities....");
    }
}
