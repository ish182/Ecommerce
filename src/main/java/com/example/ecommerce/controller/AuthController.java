package com.example.ecommerce.controller;

import com.example.ecommerce.Repository.UserRepository;
import com.example.ecommerce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // looks for login.html in templates folder
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";  // looks for register.html
    }

    @PostMapping("/doRegister")
    public String doRegister(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER"); // default role
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/default")
    public String defaultAfterLogin(Authentication authentication) {
        // check the user role
        String role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
 }
}
}