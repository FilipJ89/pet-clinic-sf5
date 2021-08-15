package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.config.securityPermissions.CreateOwnerPermission;
import com.example.petclinicsf5.config.securityPermissions.ReadOwnersPermission;
import com.example.petclinicsf5.config.securityPermissions.UpdateOwnerPermission;
import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.model.security.Role;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.services.OwnerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/owners")
@Controller
public class OwnerController {
    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ReadOwnersPermission
    @RequestMapping("/find")
    public String findOwners(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("owner", Owner.builder().build());
        if (hasUserThisRole(user,"OWNER")) {
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
        if (hasUserThisRole(user,"OWNER")) {
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
    public String showOwner(@PathVariable Long ownerId, Model model, @AuthenticationPrincipal User user) {
        if (hasUserThisRole(user, "OWNER")) {
            if (!isIdMatched(ownerId,user)) {
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
    public String initUpdateOwnerForm(@PathVariable Long ownerId, Model model, @AuthenticationPrincipal User user) {
        if (hasUserThisRole(user, "OWNER")) {
            if (!isIdMatched(ownerId,user)) {
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

    private boolean hasUserThisRole(User user, String roleName) {
        return user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList())
                .contains(roleName);
    }

    private boolean isIdMatched(Long id, User user) {
        return ownerService.findById(id).getEmail().equals(user.getEmail());
    }

}