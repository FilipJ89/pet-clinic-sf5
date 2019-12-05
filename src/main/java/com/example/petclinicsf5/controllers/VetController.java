package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.services.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VetController {

    private final VetService vetService;

    @Autowired
    public VetController(VetService vetService) {
        this.vetService = vetService;
    }

    @RequestMapping({"/vets", "vets/index", "vets/index.html"})
    public String getVetMainPage(Model model) {
        model.addAttribute("vets", vetService.findAll());
        return "vets/index";
    }
}
