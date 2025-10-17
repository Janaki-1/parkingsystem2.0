package com.example.parking.controller;

import com.example.parking.model.User;
import com.example.parking.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepo) { this.userRepo = userRepo; }

    @GetMapping({"/","/login"})
    public String loginPage(@RequestParam(value="error", required=false) String error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, Model model) {
        var opt = userRepo.findByUsername(username);
        if (opt.isPresent()) {
            User u = opt.get();
            if (encoder.matches(password, u.getPasswordHash())) {
                // set session
                session.setAttribute("currentUser", u);
                if (u.isAdmin()) return "redirect:/admin";
                return "redirect:/dashboard";
            }
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() { return "signup"; }

    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String fullName,
                         @RequestParam String password,
                         Model model, HttpSession session) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("error","Username and password required");
            return "signup";
        }
        if (userRepo.existsByUsername(username)) {
            model.addAttribute("error","Username already taken");
            return "signup";
        }
        User u = new User(username, encoder.encode(password), fullName, false);
        userRepo.save(u);
        session.setAttribute("currentUser", u);
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}