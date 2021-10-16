package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.config.securityPermissions.CreateOrUpdateVisitPermission;
import com.example.petclinicsf5.config.securityPermissions.ReadVisitPermission;
import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.model.Pet;
import com.example.petclinicsf5.model.Visit;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.services.PetService;
import com.example.petclinicsf5.services.ValidationFunctions;
import com.example.petclinicsf5.services.VisitService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/owners/{ownerId}/pets/{petId}")
public class VisitController {

    private static final String VISIT_CREATE_OR_UPDATE_FORM = "pets/createOrUpdateVisitForm";

    private final VisitService visitService;
    private final PetService petService;
    private final ValidationFunctions validationFunctions;

    public VisitController(VisitService visitService, PetService petService, ValidationFunctions validationFunctions) {
        this.visitService = visitService;
        this.petService = petService;
        this.validationFunctions = validationFunctions;
    }

    @ReadVisitPermission
    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") Long petId,Model model) {

        Pet pet = petService.findById(petId);
        model.addAttribute("pet", pet);
        Visit visit = Visit.builder().build();
        pet.getVisits().add(visit);
        visit.setPet(pet);
        return visit;
    }

    @CreateOrUpdateVisitPermission
    @GetMapping("/visits/new")
    public String initNewVisitForm(@PathVariable("petId") Long petId, @PathVariable("ownerId") Long ownerId,
                                   @AuthenticationPrincipal User user, final RedirectAttributes redirectAttributes) {
        if (validationFunctions.hasUserThisRole(user, "OWNER")) {
            Owner petOwner = petService.findById(petId).getOwner();
            if (!validationFunctions.isUserOwnerIdMatched(ownerId,user) ||
                    !validationFunctions.isUserOwnerIdMatched(petOwner.getId(),user)){
                redirectAttributes.addFlashAttribute("redirectionError", "errorVisit");
                return "redirect:/owners/find";
            }
        }
        return VISIT_CREATE_OR_UPDATE_FORM;
    }

    @CreateOrUpdateVisitPermission
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
