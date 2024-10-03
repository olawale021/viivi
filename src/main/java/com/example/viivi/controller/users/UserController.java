package com.example.viivi.controller.users;

import com.example.viivi.models.users.UserModel;
import com.example.viivi.models.users.Role;
import com.example.viivi.models.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute UserModel userModel, RedirectAttributes redirectAttributes) {
        // Check if the email already exists in the database
        if (userRepository.findByEmail(userModel.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("error", "Email already exists. Please use a different email.");
            return "redirect:/users/register";
        }

        if (userModel.getRole() == null) {
            userModel.setRole(Role.GENERAL_USER);
        }
        
        // Use PasswordEncoder to encode the password
        String encodedPassword = passwordEncoder.encode(userModel.getPassword());
        userModel.setPassword(encodedPassword);
        
        userRepository.save(userModel);
        redirectAttributes.addFlashAttribute("message", "User registered successfully. Please log in.");
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

}