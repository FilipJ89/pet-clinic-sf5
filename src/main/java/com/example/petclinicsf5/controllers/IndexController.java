package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.model.security.Role;
import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.repositories.security.RoleRepository;
import com.example.petclinicsf5.repositories.security.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class IndexController {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public IndexController(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", User.builder().build());
        return "register";
    }

    @PostMapping("/register")
    public String processRegisterForm(@Valid User user, BindingResult result, final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        } else {
            User userToSave = new User();
            userToSave.setUsername(user.getUsername());
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
            userToSave.setEmail(user.getEmail());

            Role ownerRole = roleRepository.findByName("OWNER").orElseThrow(null);
            Set <Role> addOwnerRole = new HashSet<>(Arrays.asList(ownerRole));
            userToSave.setRoles(addOwnerRole);
            userRepository.save(userToSave);

            redirectAttributes.addFlashAttribute("indexMessage",
                    "New user: '" + userToSave.getUsername() + "' has been registered. You can now log");
            return "redirect:/";
        }
    }
}
