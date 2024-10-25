package com.example.viivi.controller.cart;

import com.example.viivi.models.cart.CartItemModel;
import com.example.viivi.models.cart.CartModel;
import com.example.viivi.models.cart.CartRepository;
import com.example.viivi.models.cart.CartItemRepository;
import com.example.viivi.models.products.ProductModel;
import com.example.viivi.models.products.ProductRepository;
import com.example.viivi.models.userActivity.UserActivityModel;
import com.example.viivi.models.userActivity.UserActivityRepository;
import com.example.viivi.models.users.UserModel;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    // Show user's cart
@GetMapping
public String viewCart(@AuthenticationPrincipal UserModel user, Model model) {
    Optional<CartModel> cartOptional = cartRepository.findByUserIdWithItems(user.getId());
    
    if (cartOptional.isPresent()) {
        CartModel cart = cartOptional.get();
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            int totalQuantity = cart.getItems().stream()
                                    .mapToInt(CartItemModel::getQuantity)
                                    .sum();
            
            model.addAttribute("cart", cart);
            model.addAttribute("totalQuantity", totalQuantity);
        } else {
            model.addAttribute("cart", null);
            model.addAttribute("message", "Your cart is empty.");
        }
    } else {
        model.addAttribute("cart", null);
        model.addAttribute("message", "Your cart is empty.");
    }

    return "cart";
}

    @Transactional
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam int quantity, @AuthenticationPrincipal UserModel user, Model model) {
        Optional<ProductModel> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            model.addAttribute("error", "Product not found");
            return "redirect:/products";
        }

        ProductModel product = productOptional.get();
        CartModel cart = cartRepository.findByUserWithItems(user)
                .orElse(new CartModel(user, null, 0.0));

        // Check if item is already in the cart
        Optional<CartItemModel> existingItemOptional = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItemOptional.isPresent()) {
            CartItemModel existingItem = existingItemOptional.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItemModel cartItem = new CartItemModel(cart, product, quantity, product.getPrice());
            cart.addItem(cartItem);
            cartItemRepository.save(cartItem);
        }

        cartRepository.save(cart);

        // Log activity after cart operation
        logUserActivity(
                user.getId(),
                productId,
                "add_to_cart",
                product.getCategoryId(), // Product category ID
                null, // Activity duration can be set as needed, e.g., from session or defaults
                null, // Min price filter
                null, // Max price filter
                null, // Category filter
                user.getTopCategory1(), // User's top category ID
                user.getTopCategory2(), // User's top category ID
                user.getTopCategory3(), // User's top category ID
                null // Search filter, if applicable
        );

        return "redirect:/products/" + productId;
    }

    private void logUserActivity(Long userId, Long productId, String activityType, Long productCategoryId,
                                 Integer activityDuration, Double minPriceFilter, Double maxPriceFilter,
                                 Long categoryFilter, Long topCategory1, Long topCategory2, Long topCategory3,
                                 String searchFilter) {

        UserActivityModel activity = new UserActivityModel();

        // Set properties for user activity
        activity.setUserId(userId);
        activity.setProductId(productId);
        activity.setActivityType(activityType);
        activity.setProductCategoryId(productCategoryId);
        activity.setActivityDuration(activityDuration);
        activity.setMinPriceFilter(minPriceFilter);
        activity.setMaxPriceFilter(maxPriceFilter);
        activity.setCategoryFilter(categoryFilter);
        activity.setTopCategory1(topCategory1); // Set top category ID
        activity.setTopCategory2(topCategory2); // Set top category ID
        activity.setTopCategory3(topCategory3); // Set top category ID
        activity.setSearchFilter(searchFilter);
        activity.setActivityTimestamp(LocalDateTime.now()); // Set current timestamp

        // Save the activity to the database
        userActivityRepository.save(activity);
    }



    // Remove item from cart
    @PostMapping("/remove/{cartItemId}")
    @Transactional  // Ensure the session stays open during the transaction
    public String removeFromCart(@PathVariable Long cartItemId, @AuthenticationPrincipal UserModel user, Model model) {
        Optional<CartModel> cartOptional = cartRepository.findByUserIdWithItems(user.getId());

        if (cartOptional.isPresent()) {
            CartModel cart = cartOptional.get();

            Optional<CartItemModel> cartItemOptional = cartItemRepository.findById(cartItemId);

            if (cartItemOptional.isPresent()) {
                CartItemModel cartItem = cartItemOptional.get();
                cart.removeItem(cartItem);  // This should remove the item from the list
                cartItemRepository.delete(cartItem);  // Then, delete the cart item from the database

                // Log activity after removing the item from the cart
                logUserActivity(
                        user.getId(),
                        cartItem.getProduct().getId(),  // Get product ID from the cart item
                        "remove_from_cart",  // Interaction type
                        cartItem.getProduct().getCategoryId(), // Assuming you have a method to get the product category ID
                        null,  // Activity duration can be set as needed
                        null,  // Min price filter
                        null,  // Max price filter
                        null,  // Category filter
                        user.getTopCategory1(), // User's top category ID
                        user.getTopCategory2(),
                        user.getTopCategory3(),
                        null // Search filter, if applicable
                );
            }
        }

        return "redirect:/cart";
    }


    // Update item quantity in cart
    @PostMapping("/update/{cartItemId}")
    public String updateCartItem(@PathVariable Long cartItemId, @RequestParam int quantity) {
        Optional<CartItemModel> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartItemOptional.isPresent()) {
            CartItemModel cartItem = cartItemOptional.get();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return "redirect:/cart";
    }

    @ModelAttribute("cartQuantity")
    public int cartQuantity(@AuthenticationPrincipal UserModel user) {
        if (user != null) {
            Optional<CartModel> cartOptional = cartRepository.findByUserId(user.getId());
            if (cartOptional.isPresent()) {
                CartModel cart = cartOptional.get();
                return cart.getItems().stream().mapToInt(CartItemModel::getQuantity).sum();
            }
        }
        return 0; // Default to 0 if the user has no cart or is not logged in
    }
}
