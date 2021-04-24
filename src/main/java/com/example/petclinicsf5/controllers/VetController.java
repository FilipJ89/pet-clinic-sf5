package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.model.Vet;
import com.example.petclinicsf5.services.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
public class VetController {

    private final VetService vetService;

    @Autowired
    public VetController(VetService vetService) {
        this.vetService = vetService;
    }

    @RequestMapping({"/vets", "vets/index", "vets/index.html", "vets.html"})
    public String getVetMainPage(Model model) {
        model.addAttribute("vets", vetService.findAll());
        return "vets/index";
    }

    @GetMapping({"/api/vets", "/api/vets.html"})
    public @ResponseBody Set<Vet> getVetsJson() {
        return vetService.findAll();
    }
}
