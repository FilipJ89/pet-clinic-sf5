package com.example.petclinicsf5.controllers;

import com.example.petclinicsf5.model.security.User;
import com.example.petclinicsf5.repositories.security.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final GoogleAuthenticator googleAuthenticator;

    @GetMapping("/register2fa")
    public String register2fa(Model model, final RedirectAttributes redirectAttributes) {
        User authenticatedUser = getAuthenticatedUser();
        //Since context does not reload, get user data from database to check below condition
        User user = userRepository.findById(authenticatedUser.getId()).orElseThrow(null);

        if (user.getIsUser2fa()) {
            log.debug("User '" + user.getUsername() + "' opting out of 2FA...");

            user.setIsUser2fa(false);
            user.setIsUser2faRequired(true);
            user.setUser2faSecret(null);
            userRepository.save(user);

            log.debug("2FA for user '" + user.getUsername() + "' has been DEACTIVATED");
            redirectAttributes.addFlashAttribute("indexMessage", "You have deactivated 2FA authentication");
            return "redirect:/";
        } else {
            log.debug("User '" + user.getUsername() + "' opting in for 2FA...");

            String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL("Pet Clinic", user.getUsername(),
                    googleAuthenticator.createCredentials(user.getUsername()));
            model.addAttribute("googleurl", url);
            return "users/register2fa";
        }
    }

    @PostMapping("/register2fa")
    public String register2fa(@RequestParam(name = "verifyCode") Integer code, final RedirectAttributes redirectAttributes) {

        User user = getAuthenticatedUser();
        log.debug("2FA code entered is: " + code);
        if (googleAuthenticator.authorizeUser(user.getUsername(),code)) {
            log.debug("2FA registration success...");
            User userFromDatabase = userRepository.findById(user.getId()).orElseThrow(null);
            userFromDatabase.setIsUser2fa(true);
            userRepository.save(userFromDatabase);
            log.debug("2FA for user '" + user.getUsername() + "' has been ACTIVATED");

            redirectAttributes.addFlashAttribute("indexMessage", "You have activated 2FA authentication");
            return "redirect:/";
        } else {
            log.debug("2FA registration validation unsuccessful... returning to 2FA");
            return "users/register2fa";
        }
    }


    @GetMapping("/verify2fa")
    public String verify2fa(){
        return "users/verify2fa";
    }

    @PostMapping("/verify2fa")
    public String verifyPostOf2Fa(@RequestParam Integer verifyCode){

        User user = getAuthenticatedUser();

        if (googleAuthenticator.authorizeUser(user.getUsername(), verifyCode)) {
            ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setIsUser2faRequired(false);
            log.debug("2FA verification SUCCESS for user '" + user.getUsername() + "'");
            return "/index";
        } else {
            log.debug("2FA verification validation unsuccessful... returning to 2FA");
            return "users/verify2fa";
        }
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
