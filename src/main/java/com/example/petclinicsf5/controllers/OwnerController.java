package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.model.Owner;
import com.example.petclinicsf5.services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/owners")
@Controller
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

//    @RequestMapping({"", "/", "/index", "/index.html"})
//    public String showAllOwners(Model model) {
//        model.addAttribute("owners", ownerService.findAll());
//        return "owners/index";
//    }

    @GetMapping("/find")
    public String findOwners(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return "owners/findOwners";
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String findOwnersByLastNameLike(Owner owner, BindingResult bindingResult, Model model) {

        // All owners
        if (owner.getLastName() == null) {
            owner.setLastName(""); // empty String = broadest search
        }

        //Find owners by last name
        List<Owner> results = ownerService.findAllByLastNameContaining(owner.getLastName());

        if (results.isEmpty()) {
            bindingResult.rejectValue("lastName", "not found", "not found");
            return "owners/findOwners";
        } else if (results.size() == 1) {
            owner = results.get(0);
            return "redirect:/owners/" + owner.getId();
        } else {
            model.addAttribute("selections", results);
            return "owners/ownerList";
        }
    }

    @GetMapping("/{ownerId}")
    public String showOwnerDetails(@PathVariable("ownerId") Long id, Model model) {
        model.addAttribute(ownerService.findById(id));
        return "owners/ownerDetails";
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        } else {
            ownerService.save(owner);
            return "redirect:/owners/" + owner.getId();
        }
    }

    @GetMapping("/{ownerId}/edit")
    public String initUpdateForm(@PathVariable("ownerId") Long ownerID, Model model) {
        model.addAttribute(ownerService.findById(ownerID));
        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/{ownerId}/edit")
    public String processUpdateForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") Long ownerId) {
        if (result.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        } else {
            owner.setId(ownerId);
            ownerService.save(owner);
            return "redirect:/owners/" + owner.getId();
        }
    }

}
