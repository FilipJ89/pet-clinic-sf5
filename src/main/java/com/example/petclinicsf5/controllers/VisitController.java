package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.model.Pet;
import com.example.petclinicsf5.model.Visit;
import com.example.petclinicsf5.services.PetService;
import com.example.petclinicsf5.services.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/owners/{ownerId}/pets/{petId}")
public class VisitController {

    private static final String VISIT_CREATE_OR_UPDATE_FORM = "pets/createOrUpdateVisitForm";

    private final VisitService visitService;
    private final PetService petService;

    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") Long petId, Model model) {
        Pet pet = petService.findById(petId);
        model.addAttribute("pet", pet);
        Visit visit = Visit.builder().build();
        pet.getVisits().add(visit);
        visit.setPet(pet);
        return visit;
    }

    @GetMapping("/visits/new")
    public String initNewVisitForm(@PathVariable("petId") Long petId, Model model) {
        return VISIT_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/visits/new")
    public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return VISIT_CREATE_OR_UPDATE_FORM;
        } else {
            visitService.save(visit);
            return "redirect:/owners/{ownerId}";
        }
    }


}
