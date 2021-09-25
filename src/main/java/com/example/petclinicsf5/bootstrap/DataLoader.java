package com.example.petclinicsf5.bootstrap;

import com.example.petclinicsf5.model.*;
import com.example.petclinicsf5.model.security.Authority;
import com.example.petclinicsf5.model.security.Role;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.repositories.security.AuthorityRepository;
import com.example.petclinicsf5.repositories.security.RoleRepository;
import com.example.petclinicsf5.repositories.security.UserRepository;
import com.example.petclinicsf5.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialityService specialtyService;
    private final VisitService visitService;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(OwnerService ownerService, VetService vetService, PetTypeService petTypeService,
                      SpecialityService specialtyService, VisitService visitService, AuthorityRepository authorityRepository,
                      UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialtyService = specialtyService;
        this.visitService = visitService;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        int count = petTypeService.findAll().size();
        if (count == 0 ){
            loadSecurityData();
            loadUserData();
            loadWebData();
        }
    }

    private void loadSecurityData() {

        // Admin privilages
        Authority admin = authorityRepository.save(Authority.builder().permission("admin").build());

        // Vet auths
        Authority readVets = authorityRepository.save(Authority.builder().permission("vets.read").build());

        // Visit auths
        Authority createOrUpdateVisit = authorityRepository.save(Authority.builder().permission("visit.create/update").build());
        Authority readVisit = authorityRepository.save(Authority.builder().permission("visit.read").build());

        //Pet auths
        Authority createPet = authorityRepository.save(Authority.builder().permission("pet.create").build());
        Authority updatePet = authorityRepository.save(Authority.builder().permission("pet.update").build());

        // Owner auths
        Authority createOwner = authorityRepository.save(Authority.builder().permission("owner.create").build());
        Authority readOwner = authorityRepository.save(Authority.builder().permission("owner.read").build());
        Authority updateOwner = authorityRepository.save(Authority.builder().permission("owner.update").build());
        Authority deleteOwner = authorityRepository.save(Authority.builder().permission("owner.delete").build());


        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        adminRole.setAuthorities(new HashSet<>(Arrays.asList(admin, readVets, createOrUpdateVisit, readVisit, createPet, updatePet,
                createOwner, readOwner, updateOwner, deleteOwner)));

        Role vetRole = roleRepository.save(Role.builder().name("VET").build());
        vetRole.setAuthorities(new HashSet<>(Arrays.asList(readVets, readVisit, readOwner)));

        Role userRole = roleRepository.save(Role.builder().name("OWNER").build());
        userRole.setAuthorities(new HashSet<>(Arrays.asList(readVets, createOrUpdateVisit, readVisit, createPet, updatePet,
                createOwner, readOwner, updateOwner, deleteOwner)));

        roleRepository.saveAll(Arrays.asList(adminRole, vetRole, userRole));
    }

    private void loadUserData() {

        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(null);
        Role vetRole = roleRepository.findByName("VET").orElseThrow(null);
        Role userRole = roleRepository.findByName("OWNER").orElseThrow(null);

        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("filip-jozwiakowski@gmail.com")
                .role(adminRole)
                .build());

        userRepository.save(User.builder()
                .username("SamX")
                .password(passwordEncoder.encode("password"))
                .email("sam-axe@yahoo.com")
                .role(vetRole)
                .build());

        userRepository.save(User.builder()
                .username("JesPort95")
                .password(passwordEncoder.encode("password"))
                .email("Jess95Port@gmail.com")
                .role(vetRole)
                .build());

        userRepository.save(User.builder()
                .username("MichaelW89")
                .password(passwordEncoder.encode("password"))
                .email("michael.weston@gmail.com")
                .role(userRole)
                .build());

        userRepository.save(User.builder()
                .username("FioGle")
                .password(passwordEncoder.encode("password"))
                .email("fiona.gle@gmail.com")
                .role(userRole)
                .build());

        System.out.println("Loaded " + userRepository.count() + " users...");
    }

    private void loadWebData() {

        final String FIONA_MAIL = "fiona.gle@gmail.com";
        final String MIKE_EMAIL = "michael.weston@gmail.com";

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
        owner1.setEmail(userRepository.findByEmail(MIKE_EMAIL).map(User::getEmail).orElseThrow(null));
        owner1.setUser(userRepository.findByEmail(MIKE_EMAIL).orElseThrow(null));

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
        owner2.setEmail(userRepository.findByEmail(FIONA_MAIL).map(User::getEmail).orElseThrow(null));
        owner2.setUser(userRepository.findByEmail(FIONA_MAIL).orElseThrow(null));

        Pet fionasCat = new Pet();
        fionasCat.setName("Just Cat");
        fionasCat.setOwner(owner2);
        fionasCat.setBirthDate(LocalDate.now());
        fionasCat.setPetType(savedCatPetType);
        owner2.getPets().add(fionasCat);

        ownerService.save(owner2);

        Owner owner3 = new Owner();
        owner3.setFirstName("Keira");
        owner3.setLastName("Westrock");
        owner3.setAddress("123 Brickerel");
        owner3.setCity("Miami");
        owner3.setTelephone("4434534365");
        owner3.setEmail(userRepository.findByEmail(MIKE_EMAIL).map(User::getEmail).orElseThrow(null));
        owner3.setUser(userRepository.findByEmail(MIKE_EMAIL).orElseThrow(null));

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
    }
}
