package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.config.securityPermissions.CreatePetPermission;
import com.example.petclinicsf5.config.securityPermissions.UpdatePetPermission;
import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.model.Pet;
import com.example.petclinicsf5.model.PetType;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.services.OwnerService;
import com.example.petclinicsf5.services.PetService;
import com.example.petclinicsf5.services.PetTypeService;
import com.example.petclinicsf5.services.ValidationFunctions;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private static final String PETS_CREATE_UPDATE_FORM = "pets/createOrUpdatePetForm";

    private final PetService petService;
    private final OwnerService ownerService;
    private final PetTypeService petTypeService;
    private final ValidationFunctions validationFunctions;

    public PetController(PetService petService, OwnerService ownerService,
                         PetTypeService petTypeService, ValidationFunctions validationFunctions) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.petTypeService = petTypeService;
        this.validationFunctions = validationFunctions;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @CreatePetPermission
    @GetMapping("/pets/new")
    public String initCreationForm(Owner owner, Model model,@AuthenticationPrincipal User user,
                                   final RedirectAttributes redirectAttributes,
                                   @PathVariable("ownerId") Long ownerId) {

        if (validationFunctions.hasUserThisRole(user, "OWNER")) {
            if (!validationFunctions.isUserOwnerIdMatched(ownerId,user)) {
                redirectAttributes.addFlashAttribute("redirectionError", "You do not have permission to add new pet to this user");
                return "redirect:/owners/find";
            }
        }

        Pet pet = Pet.builder().build();
        owner.getPets().add(pet);
        pet.setOwner(owner);
        model.addAttribute("pet", pet);
        return PETS_CREATE_UPDATE_FORM;
    }

    @CreatePetPermission
    @PostMapping("/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
            result.rejectValue("name", "duplicate", "already exists");
        }
        owner.getPets().add(pet);
        if (result.hasErrors()) {
            model.addAttribute("pet", pet);
            return PETS_CREATE_UPDATE_FORM;
        } else {
            pet.setOwner(owner);
            petService.save(pet);
            return "redirect:/owners/{ownerId}";
        }
    }

    @UpdatePetPermission
    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable("petId") Long petId, @PathVariable("ownerId") Long ownerId, Model model,
                                 @AuthenticationPrincipal User user, final RedirectAttributes redirectAttributes) {

        if (validationFunctions.hasUserThisRole(user, "OWNER")) {
            if (!validationFunctions.isUserOwnerIdMatched(ownerId,user) ||
                    !validationFunctions.isUserOwnerIdMatched(validationFunctions.getOwnerByPetId(petId).getId(),user)){
                redirectAttributes.addFlashAttribute("redirectionError", "You do not have permission to edit pet for this user");
                return "redirect:/owners/find";
            }
        }

        model.addAttribute("pet", petService.findById(petId));
        return PETS_CREATE_UPDATE_FORM;
    }

    @UpdatePetPermission
    @PostMapping("/pets/{petId}/edit")
    public  String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, Model model) {
        if (result.hasErrors()) {
            pet.setOwner(owner);
            model.addAttribute("pet", pet);
            return PETS_CREATE_UPDATE_FORM;
        } else {
            pet.setOwner(owner);
            petService.save(pet);
            return "redirect:/owners/" + owner.getId();
        }
    }

}
