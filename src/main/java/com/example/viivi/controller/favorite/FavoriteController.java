package com.example.viivi.controller.favorite;


import com.example.viivi.models.favorite.FavoriteModel;
import com.example.viivi.models.products.ProductModel;
import com.example.viivi.models.users.UserModel;
import com.example.viivi.models.favorite.FavoriteRepository;
import com.example.viivi.models.products.ProductRepository;
import com.example.viivi.models.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add product to favorites
    @PostMapping("/add")
    public String addFavorite(@RequestParam Long userId, @RequestParam Long productId, Model model) {
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<ProductModel> productOptional = productRepository.findById(productId);

        if (userOptional.isPresent() && productOptional.isPresent()) {
            UserModel user = userOptional.get();
            ProductModel product = productOptional.get();

            // Check if already favorited
            Optional<FavoriteModel> existingFavorite = favoriteRepository.findByUserAndProduct(user, product);
            if (existingFavorite.isPresent()) {
                model.addAttribute("message", "Product is already in favorites.");
                return "redirect:/products/" + productId;  // Redirect back to product page
            }

            // Add to favorites
            FavoriteModel favorite = new FavoriteModel(user, product);
            favoriteRepository.save(favorite);

            model.addAttribute("message", "Product added to favorites successfully.");
            return "redirect:/products/" + productId;  // Redirect back to product page
        }

        model.addAttribute("message", "User or product not found.");
        return "redirect:/products/" + productId;  // Redirect back to product page if error
    }

    // Get all favorites for a user and return a Thymeleaf template
    @GetMapping
    public String getUserFavorites(@AuthenticationPrincipal UserModel currentUser, Model model) {
        if (currentUser != null) {
            List<FavoriteModel> favorites = favoriteRepository.findAllByUser(currentUser);
            model.addAttribute("favorites", favorites);
            model.addAttribute("user", currentUser);  // Add user to the model
            return "favorites";  // Return the Thymeleaf template for the favorites page
        }
        return "redirect:/login";  // Redirect to login if no user is logged in
    }

    // Remove product from favorites
    @PostMapping("/remove")
    public String removeFavorite(@RequestParam Long userId, @RequestParam Long productId, Model model) {
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<ProductModel> productOptional = productRepository.findById(productId);

        if (userOptional.isPresent() && productOptional.isPresent()) {
            UserModel user = userOptional.get();
            ProductModel product = productOptional.get();

            // Check if the favorite exists
            Optional<FavoriteModel> favoriteOptional = favoriteRepository.findByUserAndProduct(user, product);
            if (favoriteOptional.isPresent()) {
                favoriteRepository.delete(favoriteOptional.get());
                model.addAttribute("message", "Product removed from favorites successfully.");
            } else {
                model.addAttribute("message", "Product not found in favorites.");
            }

            return "redirect:/favorites";  // Redirect back to the favorites page
        }

        model.addAttribute("message", "User or product not found.");
        return "redirect:/favorites";  // Redirect back to the favorites page if error
    }

    @PostMapping("/toggle")
public String toggleFavorite(@RequestParam Long userId, @RequestParam Long productId, @RequestParam(required = false) Long favoriteId, Model model) {
    Optional<UserModel> userOptional = userRepository.findById(userId);
    Optional<ProductModel> productOptional = productRepository.findById(productId);

    if (userOptional.isPresent() && productOptional.isPresent()) {
        UserModel user = userOptional.get();
        ProductModel product = productOptional.get();

        // Check if the product is already in the user's favorites
        Optional<FavoriteModel> existingFavorite = favoriteRepository.findByUserAndProduct(user, product);

        if (existingFavorite.isPresent()) {
            // If it's a favorite, remove it using the favorite ID
            favoriteRepository.deleteById(existingFavorite.get().getId());
            model.addAttribute("message", "Product removed from favorites.");
        } else {
            // Otherwise, add it as a favorite
            FavoriteModel favorite = new FavoriteModel(user, product);
            favoriteRepository.save(favorite);
            model.addAttribute("message", "Product added to favorites.");
        }

        return "redirect:/products/" + productId;  // Redirect back to product page
    }

    model.addAttribute("message", "User or product not found.");
    return "redirect:/products/" + productId;  // Redirect back to product page if error
}

}
