package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.model.security.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
public class IndexController {

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping({"", "/"})
    public String showIndex(){
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model){
        model.addAttribute("user", User.builder().build());
        return "login";
    }
}
