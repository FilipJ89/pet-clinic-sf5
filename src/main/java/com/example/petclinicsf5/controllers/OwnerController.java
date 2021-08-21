package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.config.securityPermissions.CreateOwnerPermission;
import com.example.petclinicsf5.config.securityPermissions.ReadOwnersPermission;
import com.example.petclinicsf5.config.securityPermissions.UpdateOwnerPermission;
import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.services.OwnerService;
import com.example.petclinicsf5.services.ValidationFunctions;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/owners")
@Controller
public class OwnerController {
    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

    private final OwnerService ownerService;
    private final ValidationFunctions validationFunctions;

    public OwnerController(OwnerService ownerService, ValidationFunctions validationFunctions) {
        this.ownerService = ownerService;
        this.validationFunctions = validationFunctions;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ReadOwnersPermission
    @RequestMapping("/find")
    public String findOwners(Model model, @AuthenticationPrincipal User user, @ModelAttribute("redirectionError") String error){
        model.addAttribute("owner", Owner.builder().build());
        model.addAttribute("errorMessage",error);
        if (validationFunctions.hasUserThisRole(user,"OWNER")) {
            model.addAttribute("owners",ownerService.findAllByEmail(user.getEmail()));
        } else {
            model.addAttribute("owners",ownerService.findAll());
        }
        return "owners/findOwners";
    }

    @ReadOwnersPermission
    @GetMapping
    public String processFindForm(Owner owner, BindingResult result, Model model, @AuthenticationPrincipal User user){
        // allow parameter-less GET request for /owners to return all records
        if (owner.getLastName() == null) {
            owner.setLastName(""); // empty string signifies broadest possible search
        }

        List<Owner> results;
        if (validationFunctions.hasUserThisRole(user,"OWNER")) {
            results = ownerService.findAllByLastNameLikeIgnoreCaseAndEmail("%"+ owner.getLastName() + "%",user.getEmail());
        } else {
            results = ownerService.findAllByLastNameLikeIgnoreCase("%" + owner.getLastName() + "%");
        }

        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        } else if (results.size() == 1) {
            // 1 owner found
            owner = results.get(0);
            return "redirect:/owners/" + owner.getId();
        } else {
            // multiple owners found
            model.addAttribute("owners", results);
            return "owners/findOwners";
        }
    }

    @ReadOwnersPermission
    @GetMapping("/{ownerId}")
    public String showOwner(@PathVariable Long ownerId, Model model, @AuthenticationPrincipal User user,
                            final RedirectAttributes redirectAttributes) {
        if (validationFunctions.hasUserThisRole(user, "OWNER")) {
            if (!validationFunctions.isUserOwnerIdMatched(ownerId,user)) {
                redirectAttributes.addFlashAttribute("redirectionError", "You do not have permission to read this user details");
                return "redirect:/owners/find";
            }
        }
        model.addAttribute("owner",ownerService.findById(ownerId));
        return "owners/ownerDetails";
    }

    @CreateOwnerPermission
    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @CreateOwnerPermission
    @PostMapping("/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            Owner savedOwner =  ownerService.save(owner);
            return "redirect:/owners/" + savedOwner.getId();
        }
    }

    @UpdateOwnerPermission
    @GetMapping("/{ownerId}/edit")
    public String initUpdateOwnerForm(@PathVariable Long ownerId, Model model, @AuthenticationPrincipal User user,
                                      final RedirectAttributes redirectAttributes) {
        if (validationFunctions.hasUserThisRole(user, "OWNER")) {
            if (!validationFunctions.isUserOwnerIdMatched(ownerId,user)) {
                redirectAttributes.addFlashAttribute("redirectionError", "You do not have permission to edit this user details");
                return "redirect:/owners/find";
            }
        }
        model.addAttribute(ownerService.findById(ownerId));
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @UpdateOwnerPermission
    @PostMapping("/{ownerId}/edit")
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable Long ownerId) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            owner.setId(ownerId);
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/" + savedOwner.getId();
        }
    }
}