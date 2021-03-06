package com.example.petclinicsf5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String showIndex(){
        return "index";
    }

    @GetMapping("/oups")
    public String showError(){
        return "notimplemented";
    }
}
