package com.example.viivi.controller.users;

import com.example.viivi.models.category.CategoryModel;
import com.example.viivi.models.category.CategoryRepository;
import com.example.viivi.models.users.UserModel;
import com.example.viivi.models.users.Role;
import com.example.viivi.models.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    @GetMapping("/preferences")
    public String showPreferencesForm(Model model, Authentication authentication) {
        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login"; // Redirect to login if not authenticated
        }

        // Fetch the logged-in user
        String userEmail = authentication.getName();
        UserModel loggedInUser = userRepository.findByEmail(userEmail);

        // Fetch the list of categories from the database
        List<CategoryModel> categories = categoryRepository.findAll();

        // Add the categories and user to the model
        model.addAttribute("categories", categories);
        model.addAttribute("user", loggedInUser);

        return "category-preferences"; // Ensure this matches your Thymeleaf template name
    }

    @PostMapping("/preferences")
    public String updatePreferences(@RequestParam(value = "preferredCategories", required = false) List<Long> preferredCategories,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login"; // Redirect to login if not authenticated
        }

        // Fetch the logged-in user
        String userEmail = authentication.getName();
        UserModel loggedInUser = userRepository.findByEmail(userEmail);

        // Set the selected categories in the user model
        if (preferredCategories != null && preferredCategories.size() > 0) {
            // Set the top categories, ensuring no more than 3 categories are saved
            loggedInUser.setTopCategory1(preferredCategories.size() > 0 ? Long.valueOf(preferredCategories.get(0).toString()) : null);
            loggedInUser.setTopCategory2(preferredCategories.size() > 1 ? Long.valueOf(preferredCategories.get(1).toString()) : null);
            loggedInUser.setTopCategory3(preferredCategories.size() > 2 ? Long.valueOf(preferredCategories.get(2).toString()) : null);
        } else {
            // Reset categories if none are selected
            loggedInUser.setTopCategory1(null);
            loggedInUser.setTopCategory2(null);
            loggedInUser.setTopCategory3(null);
        }

        // Update the user in the database
        userRepository.save(loggedInUser);

        // Add a success message and redirect back to the preferences page
        redirectAttributes.addFlashAttribute("message", "Preferences updated successfully.");
        return "redirect:/users/preferences";
    }

}